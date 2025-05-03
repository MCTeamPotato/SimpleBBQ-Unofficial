package com.sihenzhang.simplebbq.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.block.GrillBlock;
import com.sihenzhang.simplebbq.recipe.SeasoningInput;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import io.github.fabricators_of_create.porting_lib.core.util.INBTSerializable;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GrillBlockEntity extends BlockEntity {
    private static final int BURN_COOL_SPEED = 2;
    private static final int SLOT_NUM = 2;

    private final CampfireData campfireData = new CampfireData();
    private final ItemStackHandlerContainer inventory = new ItemStackHandlerContainer(SLOT_NUM) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemVariant itemVariant, int count) {
            return getCookingRecipe(itemVariant.toStack(count), level) != null;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markUpdated();
        }
    };
    private final int[] cookingProgress = new int[SLOT_NUM];
    private final int[] cookingTime = new int[SLOT_NUM];

    public GrillBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(SimpleBBQRegistry.GRILL_BLOCK_ENTITY, pWorldPosition, pBlockState);
    }

    public void initCampfireState(CampfireData data, HolderLookup.Provider registries) {
        if (level == null || data == null) {
            return;
        }
        campfireData.deserializeNBT(registries,data.serializeNBT(registries));
        var state = this.getBlockState().setValue(GrillBlock.LIT, campfireData.lit);
        level.setBlockAndUpdate(worldPosition, state);
        setChanged(level, worldPosition, state);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, GrillBlockEntity pBlockEntity) {
        var hasChanged = false;

        // sync the campfire state
        if (pState.hasProperty(GrillBlock.WATERLOGGED) && pState.getValue(GrillBlock.WATERLOGGED) && pBlockEntity.campfireData.lit) {
            pBlockEntity.campfireData.lit = false;
            hasChanged = true;
        }
        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT) != pBlockEntity.campfireData.lit) {
            pState = pState.setValue(GrillBlock.LIT, pBlockEntity.campfireData.lit);
            pLevel.setBlockAndUpdate(pPos, pState);
            hasChanged = true;
        }

        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT)) {
            for (var i = 0; i < pBlockEntity.inventory.getSlots().size(); i++) {
                var stackInSlot = pBlockEntity.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    hasChanged = true;
                    pBlockEntity.cookingProgress[i]++;
                    if (pBlockEntity.cookingProgress[i] >= pBlockEntity.cookingTime[i]) {
                        var cookingRecipe = pBlockEntity.getCookingRecipe(stackInSlot, pLevel);
                        if(cookingRecipe == null){
                            continue;
                        }
                        var result = cookingRecipe.value().assemble(new SingleRecipeInput(stackInSlot), pLevel.registryAccess());
                        CompoundTag compoundTag = stackInSlot.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
                        if (seasoningTag != null) {
                            seasoningTag.putBoolean("HasEffect", true);
                            compoundTag.put("Seasoning", seasoningTag);
                            stackInSlot.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                        }
                        Containers.dropItemStack(pLevel, pPos.getX(), (double) pPos.getY() + 0.5D, pPos.getZ(), result);
                        pBlockEntity.inventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
            }
        } else {
            for (var i = 0; i < pBlockEntity.inventory.getSlots().size(); i++) {
                var stackInSlot = pBlockEntity.inventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    hasChanged = true;
                    pBlockEntity.cookingProgress[i] = Mth.clamp(pBlockEntity.cookingProgress[i] - BURN_COOL_SPEED, 0, pBlockEntity.cookingTime[i]);
                }
            }
        }

        if (hasChanged) {
            setChanged(pLevel, pPos, pState);
        }
    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, GrillBlockEntity pBlockEntity) {
        if (pState.hasProperty(GrillBlock.LIT) && pState.getValue(GrillBlock.LIT) && pBlockEntity.campfireData.lit) {
            var random = pLevel.getRandom();

            if (random.nextFloat() < 0.05F) {
                GrillBlock.makeCampfireParticles(pLevel, pPos, false);
            }

            var facing = pState.getValue(GrillBlock.FACING);
            for (var i = 0; i < pBlockEntity.inventory.getSlots().size(); i++) {
                if (!pBlockEntity.inventory.getStackInSlot(i).isEmpty() && random.nextFloat() < 0.2F) {
                    var x = (double) pPos.getX() + 0.5D + (facing.getAxis() == Direction.Axis.Z ? (0.2D - 0.4D * i) * facing.getStepZ() : 0.0D);
                    var y = (double) pPos.getY() + 1.1D;
                    var z = (double) pPos.getZ() + 0.5D + (facing.getAxis() == Direction.Axis.X ? (0.4D * i - 0.2D) * facing.getStepX() : 0.0D);

                    for (var j = 0; j < 4; j++) {
                        pLevel.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }
        }
    }

    public ItemStackHandlerContainer getInventory() {
        return inventory;
    }

    public CampfireData getCampfireData() {
        return campfireData;
    }

    public void setCampfireData(CampfireData data, HolderLookup.Provider registries) {
        if (level != null) {
            campfireData.deserializeNBT(registries,data.serializeNBT(registries));
            this.markUpdated();
        }
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.loadAdditional(pTag, registries);
        campfireData.deserializeNBT(registries,pTag.getCompound("CampfireData"));
        inventory.deserializeNBT(registries,pTag.getCompound("Inventory"));
        if (pTag.contains("CookingTimes", Tag.TAG_INT_ARRAY)) {
            var cookingProcessArray = pTag.getIntArray("CookingTimes");
            System.arraycopy(cookingProcessArray, 0, cookingProgress, 0, Math.min(cookingProgress.length, cookingProcessArray.length));
        }
        if (pTag.contains("CookingTotalTimes", Tag.TAG_INT_ARRAY)) {
            var cookingTimeArray = pTag.getIntArray("CookingTotalTimes");
            System.arraycopy(cookingTimeArray, 0, cookingTime, 0, Math.min(cookingTime.length, cookingTimeArray.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider registries) {
        super.saveAdditional(pTag,registries);
        pTag.put("CampfireData", campfireData.serializeNBT(registries));
        pTag.put("Inventory", inventory.serializeNBT(registries));
        pTag.putIntArray("CookingTimes", cookingProgress);
        pTag.putIntArray("CookingTotalTimes", cookingTime);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var tag = new CompoundTag();
        tag.put("CampfireData", campfireData.serializeNBT(registries));
        tag.put("Inventory", inventory.serializeNBT(registries));
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Nullable
    public RecipeHolder<? extends AbstractCookingRecipe> getCookingRecipe(ItemStack itemStack, Level pLevel) {
        try {
            var grillCookingRecipe = pLevel.getRecipeManager().getRecipeFor(SimpleBBQRegistry.GRILL_COOKING_RECIPE_TYPE, new SingleRecipeInput(itemStack), pLevel);
            return grillCookingRecipe.isPresent() ? grillCookingRecipe.orElseThrow() : pLevel.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(itemStack), pLevel).orElseThrow();
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public RecipeHolder<? extends AbstractCookingRecipe> getCookableRecipe(ItemStack input) {
        for (var i = 0; i < inventory.getSlots().size(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return this.getCookingRecipe(input, level);
            }
        }
        return null;
    }

    public boolean placeFood(ItemStack input, int cookTime) {
        for (var i = 0; i < inventory.getSlots().size(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                cookingTime[i] = cookTime;
                cookingProgress[i] = 0;
                inventory.setStackInSlot(i, input.split(1));
                return true;
            }
        }
        return false;
    }

    public boolean removeFood(Player player, InteractionHand hand, boolean isHittingLeftSide) {
        if (!player.getItemInHand(hand).isEmpty()) {
            return false;
        }
        var stackInInventory = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
        if (stackInInventory.isEmpty()) {
            return false;
        }
        inventory.setStackInSlot(isHittingLeftSide ? 0 : 1, ItemStack.EMPTY);
        player.setItemInHand(hand, stackInInventory);
        return true;
    }

    @Nullable
    public RecipeHolder<SeasoningRecipe> getSeasoningRecipe(ItemStack seasoning, boolean isHittingLeftSide) {
        try {
            var input = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
            return level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SEASONING_RECIPE_TYPE, new SeasoningInput(input, seasoning), level).orElseThrow();
        }catch (Exception e){
            return null;
        }
    }

    public boolean addSeasoning(Player player, ItemStack seasoning, boolean isHittingLeftSide) {
        var input = inventory.getStackInSlot(isHittingLeftSide ? 0 : 1);
        if (input.isEmpty()) {
            return false;
        }
        var container = new SeasoningInput(input, seasoning);
        var optionalRecipe = level.getRecipeManager().getRecipeFor(SimpleBBQRegistry.SEASONING_RECIPE_TYPE, container, level);
        if (optionalRecipe.isEmpty()) {
            return false;
        }
        var recipe = optionalRecipe.get().value();
        var result = recipe.assemble(container, level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }
        inventory.setStackInSlot(isHittingLeftSide ? 0 : 1, result);
        seasoning.shrink(1);
        var remainingItems = recipe.getRemainingItems(container);
        if (!remainingItems.isEmpty()) {
            remainingItems.forEach(item -> ItemHandlerHelper.giveItemToPlayer(player, item));
        }
        return true;
    }

    private void markUpdated() {
        this.setChanged();
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public static final class CampfireData implements INBTSerializable<CompoundTag> {
        public ResourceLocation registryName = BuiltInRegistries.BLOCK.getKey(Blocks.AIR);
        public boolean lit = false;
        public Direction facing;

        public CampfireData() {
        }

        public CampfireData(BlockState state) {
            Preconditions.checkArgument(GrillBlock.isCampfire(state), "State must be a Campfire.");
            this.registryName = BuiltInRegistries.BLOCK.getKey(state.getBlock());
            this.lit = state.getValue(BlockStateProperties.LIT);
            if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                this.facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            }
        }

        public BlockState toBlockState() {
            if (registryName == null) {
                return Blocks.AIR.defaultBlockState();
            }
            var state = BuiltInRegistries.BLOCK.get(registryName).defaultBlockState();
            if (state.hasProperty(BlockStateProperties.LIT)) {
                state = state.setValue(BlockStateProperties.LIT, lit);
            }
            if (facing != null && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
            }
            return state;
        }

        public CampfireData copy(HolderLookup.Provider provider) {
            var newCampfireData = new GrillBlockEntity.CampfireData();
            newCampfireData.deserializeNBT(provider,this.serializeNBT(provider));
            return newCampfireData;
        }

        @Override
        public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putString("RegistryName", registryName.toString());
            tag.putBoolean("Lit", lit);
            if (facing != null) {
                tag.putString("Facing", facing.name());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            registryName = ResourceLocation.tryParse(nbt.getString("RegistryName"));
            lit = nbt.getBoolean("Lit");
            if (nbt.contains("Facing", Tag.TAG_STRING)) {
                facing = Direction.valueOf(nbt.getString("Facing"));
            }
        }
    }
}
