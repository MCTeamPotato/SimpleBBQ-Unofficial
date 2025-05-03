package com.sihenzhang.simplebbq.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.sihenzhang.simplebbq.SimpleBBQConfig;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.entity.GrillBlockEntity;
import com.sihenzhang.simplebbq.thirdparty.util.BlockHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.*;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Supplier;

public class GrillBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, BlockPickInteractionAware {
    protected static final VoxelShape OUTLINE_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 1.0D, 10.0D, 1.0D),
            Block.box(0.0D, 0.0D, 15.0D, 1.0D, 10.0D, 16.0D),
            Block.box(15.0D, 0.0D, 15.0D, 16.0D, 10.0D, 16.0D),
            Block.box(15.0D, 0.0D, 0.0D, 16.0D, 10.0D, 1.0D),
            Block.box(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    );
    protected static final VoxelShape COLLISION_SHAPE = Shapes.join(
            OUTLINE_SHAPE,
            Block.box(1.0D, 15.5D, 1.0D, 15.0D, 16.0D, 15.0D),
            BooleanOp.ONLY_FIRST
    );
    protected static final Supplier<Set<Item>> CAMPFIRE_ITEMS = Suppliers.memoize(() -> BuiltInRegistries.ITEM.stream().filter(item -> isCampfire(item.getDefaultInstance())).collect(ImmutableSet.toImmutableSet()));
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GrillBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(LIT, false).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemInteractionResult useItemOn(ItemStack stack,BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var stackInHand = pPlayer.getItemInHand(pHand);
            var campfireData = grillBlockEntity.getCampfireData();

            // try to place the campfire
            if (campfireData.toBlockState().isAir() && isCampfire(stackInHand)) {
                // TODO: set campfire state with stack nbt
                var campfireState = ((BlockItem) stackInHand.getItem()).getBlock().getStateForPlacement(new BlockPlaceContext(pLevel, pPlayer, pHand, stackInHand, pHit));
                var newCampfireData = new GrillBlockEntity.CampfireData(campfireState);
                grillBlockEntity.setCampfireData(newCampfireData,pLevel.registryAccess());
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, newCampfireData.lit));
                if (pPlayer instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, pPos, stackInHand);
                }
                pLevel.gameEvent(pPlayer, GameEvent.BLOCK_PLACE, pPos);
                var campfireSoundType = campfireState.getSoundType();
                pLevel.playSound(pPlayer, pPos, campfireSoundType.getPlaceSound(), SoundSource.BLOCKS, (campfireSoundType.getVolume() + 1.0F) / 2.0F, campfireSoundType.getPitch() * 0.8F);
                if (!pPlayer.getAbilities().instabuild) {
                    stackInHand.shrink(1);
                }
                return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
            }

            // try to light the grill
            if (pState.hasProperty(LIT) && !pState.getValue(LIT) && pState.hasProperty(WATERLOGGED) && !pState.getValue(WATERLOGGED) && isCampfire(campfireData.toBlockState()) && !campfireData.lit) {
                if (stackInHand.getItem() instanceof FlintAndSteelItem) {
                    pLevel.playSound(pPlayer, pPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, Mth.nextFloat(pLevel.getRandom(), 0.8F, 1.2F));
                    var newCampfireData = campfireData.copy(pLevel.registryAccess());
                    newCampfireData.lit = true;
                    grillBlockEntity.setCampfireData(newCampfireData,pLevel.registryAccess());
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_PLACE, pPos);
                    stackInHand.hurtAndBreak(1, pPlayer, EquipmentSlot.MAINHAND);
                    return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
                }
                if (stackInHand.getItem() instanceof FireChargeItem) {
                    pLevel.playSound(pPlayer, pPos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, Mth.nextFloat(pLevel.getRandom(), 0.8F, 1.2F));
                    var newCampfireData = campfireData.copy(pLevel.registryAccess());
                    newCampfireData.lit = true;
                    grillBlockEntity.setCampfireData(newCampfireData,pLevel.registryAccess());
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, true));
                    pLevel.gameEvent(pPlayer, GameEvent.BLOCK_PLACE, pPos);
                    if (!pPlayer.getAbilities().instabuild) {
                        stackInHand.shrink(1);
                    }
                    return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
                }
            }

            // try to dowse the grill
            if (pState.hasProperty(LIT) && pState.getValue(LIT) && isCampfire(campfireData.toBlockState()) && campfireData.lit && stackInHand.getItem() instanceof ShovelItem) {
                if (!pLevel.isClientSide()) {
                    pLevel.levelEvent(null, 1009, pPos, 0);
                }
                dowse(pPlayer, pLevel, pPos, pState);
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LIT, false));
                stackInHand.hurtAndBreak(1, pPlayer,  EquipmentSlot.MAINHAND);
                return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
            }

            // try to cook
            var optionalCookingRecipe = grillBlockEntity.getCookableRecipe(stackInHand);
            if (optionalCookingRecipe!=null) {
                var cookingRecipe = optionalCookingRecipe.value();
                var cookingTime = optionalCookingRecipe.value().getCookingTime();
                var actualCookingTime = cookingRecipe.getType() == RecipeType.CAMPFIRE_COOKING ? Mth.clamp((int) (cookingTime * SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_COOKING_TIME_MODIFIER.get()), Math.min(SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_MINIMUM_COOKING_TIME.get(), cookingTime), cookingTime) : cookingTime;
                if (!pLevel.isClientSide() && grillBlockEntity.placeFood(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, actualCookingTime)) {
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.CONSUME;
            }

            if (pHit.getType() == HitResult.Type.BLOCK && pHit.getDirection() == Direction.UP) {
                var clickLocation = pHit.getLocation();
                if (pState.hasProperty(FACING)) {
                    var facing = pState.getValue(FACING);
                    var isHittingLeftSide = false;
                    var isHittingRightSide = false;
                    switch (facing) {
                        case NORTH -> {
                            isHittingLeftSide = clickLocation.x - (double) pPos.getX() < 0.5D;
                            isHittingRightSide = clickLocation.x - (double) pPos.getX() > 0.5D;
                        }
                        case SOUTH -> {
                            isHittingLeftSide = clickLocation.x - (double) pPos.getX() > 0.5D;
                            isHittingRightSide = clickLocation.x - (double) pPos.getX() < 0.5D;
                        }
                        case EAST -> {
                            isHittingLeftSide = clickLocation.z - (double) pPos.getZ() < 0.5D;
                            isHittingRightSide = clickLocation.z - (double) pPos.getZ() > 0.5D;
                        }
                        case WEST -> {
                            isHittingLeftSide = clickLocation.z - (double) pPos.getZ() > 0.5D;
                            isHittingRightSide = clickLocation.z - (double) pPos.getZ() < 0.5D;
                        }
                    }
                    if (isHittingLeftSide) {
                        // try to remove from the left side
                        if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty()) {
                            if (!pLevel.isClientSide() && grillBlockEntity.removeFood(pPlayer, pHand, true)) {
                                return ItemInteractionResult.SUCCESS;
                            }
                            return ItemInteractionResult.CONSUME;
                        }
                        // try to season food from the left side
                        var optionalSeasoningRecipe = grillBlockEntity.getSeasoningRecipe(stackInHand, true);
                        if (optionalSeasoningRecipe!=null) {
                            if (!pLevel.isClientSide() && grillBlockEntity.addSeasoning(pPlayer, pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, true)) {
                                return ItemInteractionResult.SUCCESS;
                            }
                            return ItemInteractionResult.CONSUME;
                        }
                    } else if (isHittingRightSide) {
                        // try to remove from the right side
                        if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty()) {
                            if (!pLevel.isClientSide() && grillBlockEntity.removeFood(pPlayer, pHand, false)) {
                                return ItemInteractionResult.SUCCESS;
                            }
                            return ItemInteractionResult.CONSUME;
                        }
                        // try to season food from the right side
                        var optionalSeasoningRecipe = grillBlockEntity.getSeasoningRecipe(stackInHand, false);
                        if (optionalSeasoningRecipe!=null) {
                            if (!pLevel.isClientSide() && grillBlockEntity.addSeasoning(pPlayer, pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, false)) {
                                return ItemInteractionResult.SUCCESS;
                            }
                            return ItemInteractionResult.CONSUME;
                        }
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.fireImmune() && pState.hasProperty(LIT) && pState.getValue(LIT) && pEntity instanceof LivingEntity livingEntity) {
            for (ItemStack next : livingEntity.getArmorSlots()) {
                if (next.has(DataComponents.ENCHANTMENTS)) {
                    for (Holder<Enchantment> enchantmentHolder : next.get(DataComponents.ENCHANTMENTS).keySet()) {
                        if (enchantmentHolder.is(Enchantments.FIRE_PROTECTION)) {
                            return;
                        }
                    }
                }
            }

            var damage = 1.0F;
            if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
                var block = grillBlockEntity.getCampfireData().toBlockState().getBlock();
                if (block instanceof CampfireBlock campfireBlock) {
                    damage = (float) campfireBlock.fireDamage;
                }
            }
            pEntity.hurt(pLevel.damageSources().hotFloor(), damage);
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
                ItemStackHandlerContainer inventory = grillBlockEntity.getInventory();
                for(int i = 0; i < inventory.getSlots().size(); i++) {
                    Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), inventory.getStackInSlot(i));
                }
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                var hitResult = getPlayerHitResult(player);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var blockHitResult = (BlockHitResult) hitResult;
                    if (!isHittingGrill(blockHitResult)) {
                        return campfireState.getBlock().canHarvestBlock(campfireState, level, pos, player);
                    }
                }
            }
        }
        return super.canHarvestBlock(state, level, pos, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                var hitResult = getPlayerHitResult(pPlayer);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var blockHitResult = (BlockHitResult) hitResult;
                    if (!isHittingGrill(blockHitResult)) {
                        float f = pState.getDestroySpeed(pLevel, pPos);
                        if (f == -1.0F) {
                            return 0.0F;
                        } else {
                            int i = com.sihenzhang.simplebbq.thirdparty.event.EventHooks.doPlayerHarvestCheck(pPlayer, pState, pLevel, pPos) ? 30 : 100;
                            return pPlayer.getDestroySpeed(pState) / f / (float)i;
                        }
                    }
                }
            }
        }
        return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                var hitResult = getPlayerHitResult(pPlayer);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var blockHitResult = (BlockHitResult) hitResult;
                    if (!isHittingGrill(blockHitResult)) {
                        return campfireState.getBlock().playerWillDestroy(pLevel, pPos, campfireState, pPlayer);
                    }
                }
            }
        }
        return super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                var hitResult = getPlayerHitResult(player);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var blockHitResult = (BlockHitResult) hitResult;
                    this.playerWillDestroy(level, pos, state, player);
                    if (isHittingGrill(blockHitResult)) {
                        level.removeBlock(pos, false);
                        var campfireStateForPlacement = campfireState.getBlock().getStateForPlacement(new BlockPlaceContext(level, player, InteractionHand.MAIN_HAND, player.getMainHandItem(), blockHitResult));
                        // copy properties to the campfire state for placement
                        if (campfireState.hasProperty(BlockStateProperties.LIT) && campfireStateForPlacement.hasProperty(BlockStateProperties.LIT)) {
                            campfireStateForPlacement = campfireStateForPlacement.setValue(BlockStateProperties.LIT, campfireState.getValue(BlockStateProperties.LIT));
                        }
                        if (campfireStateForPlacement.hasProperty(BlockStateProperties.WATERLOGGED) && campfireStateForPlacement.getValue(BlockStateProperties.WATERLOGGED) && campfireStateForPlacement.hasProperty(BlockStateProperties.LIT)) {
                            campfireStateForPlacement = campfireStateForPlacement.setValue(BlockStateProperties.LIT, false);
                        }
                        if (campfireState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && campfireStateForPlacement.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                            campfireStateForPlacement = campfireStateForPlacement.setValue(BlockStateProperties.HORIZONTAL_FACING, campfireState.getValue(BlockStateProperties.HORIZONTAL_FACING));
                        }
                        return level.setBlock(pos, campfireStateForPlacement, level.isClientSide() ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL);
                    } else {
                        grillBlockEntity.setCampfireData(new GrillBlockEntity.CampfireData(),level.registryAccess());
                        level.setBlock(pos, state.setValue(LIT, false), level.isClientSide() ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL);
                        if (!player.isCreative() && campfireState.getBlock().canHarvestBlock(campfireState, level, pos, player)) {
                            campfireState.getBlock().playerDestroy(level, player, pos, campfireState, null, player.getMainHandItem().copy());
                        }
                        return false;
                    }
                }
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public ItemStack getPickedStack(BlockState state, BlockGetter level, BlockPos pos, Player player, HitResult target) {
        var raw = BlockHelper.getRawBlockState(state, level, pos);
        if (level.getBlockEntity(pos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                var hitResult = getPlayerHitResult(player);
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var blockHitResult = (BlockHitResult) hitResult;
                    if (!isHittingGrill(blockHitResult)) {
                        return ((BlockPickInteractionAware) campfireState.getBlock()).getPickedStack(campfireState, level, pos, player, target);
                    }
                }
            }
        }
        return ((BlockPickInteractionAware) raw.getBlock()).getPickedStack(state, level, pos, player, target);
    }

    private static HitResult getPlayerHitResult(Player pPlayer) {
        var reachDistanceAttribute = pPlayer.getAttribute(Attributes.ENTITY_INTERACTION_RANGE);
        var reachDistance = reachDistanceAttribute != null ? reachDistanceAttribute.getValue() : 5.0D;
        if (pPlayer.isCreative()) {
            reachDistance -= 0.5D;
        }
        return pPlayer.pick(reachDistance, 1.0F, false);
    }

    private static boolean isHittingGrill(BlockHitResult pHit) {
        var clickLocation = pHit.getLocation();
        var clickBlockPos = pHit.getBlockPos();
        var clickRelativeX = clickLocation.x - (double) clickBlockPos.getX();
        var clickRelativeY = clickLocation.y - (double) clickBlockPos.getY();
        var clickRelativeZ = clickLocation.z - (double) clickBlockPos.getZ();
        if ((clickRelativeX <= 0.0625D || clickRelativeX > 0.9375D) && (clickRelativeZ <= 0.0625D || clickRelativeZ > 0.9375D) && clickRelativeY <= 0.625D) {
            return true;
        }
        return clickRelativeY > 0.625D;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, false).setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER).setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getShapeWithCampfire(COLLISION_SHAPE, pState, pLevel, pPos, pContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity && grillBlockEntity.getCampfireData().toBlockState().isAir()) {
            if (pContext instanceof EntityCollisionContext entityCollisionContext) {
                if (isCampfire(entityCollisionContext.heldItem)) {
                    return Shapes.block();
                }
            } else {
                // this code block will not be invoked in theory, keep it just in case
                if (CAMPFIRE_ITEMS.get().stream().anyMatch(pContext::isHoldingItem)) {
                    return Shapes.block();
                }
            }
        }
        return this.getShapeWithCampfire(OUTLINE_SHAPE, pState, pLevel, pPos, pContext);
    }

    @SuppressWarnings("deprecation")
    private VoxelShape getShapeWithCampfire(VoxelShape pBaseShape, BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                return Shapes.or(pBaseShape, Shapes.block());
            }
        }
        return pBaseShape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return COLLISION_SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        if (pUseContext.getLevel().getBlockEntity(pUseContext.getClickedPos()) instanceof GrillBlockEntity grillBlockEntity) {
            if (grillBlockEntity.getCampfireData().toBlockState().isAir()) {
                return !pUseContext.isSecondaryUseActive() && isCampfire(pUseContext.getItemInHand()) || super.canBeReplaced(pState, pUseContext);
            }
        }
        return super.canBeReplaced(pState, pUseContext);
    }

    public static boolean isCampfire(BlockState pState) {
        return pState.is(BlockTags.CAMPFIRES) && pState.hasProperty(BlockStateProperties.LIT);
    }

    public static boolean isCampfire(ItemStack pStack) {
        return pStack != null && pStack.getItem() instanceof BlockItem blockItem && isCampfire(blockItem.getBlock().defaultBlockState());
    }

    public static final MapCodec<GrillBlock> CODEC = simpleCodec(GrillBlock::new);
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var campfireState = grillBlockEntity.getCampfireData().toBlockState();
            if (isCampfire(campfireState)) {
                campfireState.getBlock().animateTick(campfireState, pLevel, pPos, pRandom);
            }
        }
    }

    public static void dowse(@Nullable Entity pEntity, LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) {
            for (int i = 0; i < 20; i++) {
                makeCampfireParticles((Level) pLevel, pPos, true);
            }
        }
        if (pLevel.getBlockEntity(pPos) instanceof GrillBlockEntity grillBlockEntity) {
            var newCampfireData = grillBlockEntity.getCampfireData().copy(pLevel.registryAccess());
            newCampfireData.lit = false;
            grillBlockEntity.setCampfireData(newCampfireData,pLevel.registryAccess());
        }
        pLevel.gameEvent(pEntity, GameEvent.BLOCK_CHANGE, pPos);
    }

    @Override
    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (!pState.getValue(WATERLOGGED) && pFluidState.getType() == Fluids.WATER) {
            if (pState.getValue(LIT)) {
                if (!pLevel.isClientSide()) {
                    pLevel.playSound(null, pPos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                dowse(null, pLevel, pPos, pState);
            }
            pLevel.setBlock(pPos, pState.setValue(WATERLOGGED, true).setValue(LIT, false), Block.UPDATE_ALL);
            pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            return true;
        } else {
            return false;
        }
    }

    public static void makeCampfireParticles(Level pLevel, BlockPos pPos, boolean pSpawnExtraSmoke) {
        var random = pLevel.getRandom();
        var x = (double) pPos.getX() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1.0D : -1.0D);
        var y = (double) pPos.getY() + random.nextDouble() + random.nextDouble();
        var z = (double) pPos.getZ() + 0.5D + random.nextDouble() / 3.0D * (random.nextBoolean() ? 1.0D : -1.0D);
        var ySpeed = Mth.nextDouble(random, 0.015D, 0.025D);
        pLevel.addAlwaysVisibleParticle(SimpleBBQRegistry.CAMPFIRE_SMOKE_UNDER_GRILL, true, x, y, z, 0.0D, ySpeed, 0.0D);
        if (pSpawnExtraSmoke) {
            pLevel.addParticle(ParticleTypes.SMOKE, (double) pPos.getX() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pPos.getY() + 0.4D, (double) pPos.getZ() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT, WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new GrillBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return createTickerHelper(pBlockEntityType, SimpleBBQRegistry.GRILL_BLOCK_ENTITY, GrillBlockEntity::clientTick);
        }
        return createTickerHelper(pBlockEntityType, SimpleBBQRegistry.GRILL_BLOCK_ENTITY, GrillBlockEntity::serverTick);
    }

    @Override
    public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos) {
        return state.hasProperty(LIT) && state.getValue(LIT);
    }
}
