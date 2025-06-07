package com.sihenzhang.simplebbq;

import com.sihenzhang.simplebbq.block.SkeweringTableBlock;
import com.sihenzhang.simplebbq.block.entity.SkeweringTableBlockEntity;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import com.sihenzhang.simplebbq.thirdparty.event.LivingEntityUseItemEvents;
import com.sihenzhang.simplebbq.util.ModUtils;
import com.sihenzhang.simplebbq.util.VillagerTradeUtils;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SimpleBBQEvents {
    public static void initialize() {
        LivingEntityUseItemEvents.START.register(SimpleBBQEvents::onItemUseStart);
        LivingEntityUseItemEvents.FINISH.register(SimpleBBQEvents::onItemUseFinish);
        UseBlockCallback.EVENT.register(SimpleBBQEvents::onBlockRightClick);
        addVillagerTrades();
    }

    public static void addVillagerTrades() {
        TradeOfferHelper.registerVillagerOffers(SimpleBBQVillagers.SKEWERMAN, 1, noviceTrades -> {
            noviceTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.BROWN_MUSHROOM, 5, 1, SimpleBBQRegistry.MUSHROOM_SKEWER, 5, 16, 2, 0.05F));
            noviceTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.POTATO, 5, 1, SimpleBBQRegistry.POTATO_SKEWER, 5, 16, 2, 0.05F));
            noviceTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.COD, 4, 1, SimpleBBQRegistry.COD_SKEWER, 4, 16, 2, 0.05F));
            noviceTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.CHICKEN, 4, 1, SimpleBBQRegistry.CHICKEN_SKEWER, 4, 16, 2, 0.05F));
            noviceTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.RABBIT, 3, 1, SimpleBBQRegistry.RABBIT_SKEWER, 3, 16, 2, 0.05F));
        });

        TradeOfferHelper.registerVillagerOffers(SimpleBBQVillagers.SKEWERMAN, 2, apprenticeTrades -> {
            apprenticeTrades.add(VillagerTradeUtils.itemsForEmeralds(Items.BOWL, 20, 1, 16, 10, 0.05F));
            apprenticeTrades.add(VillagerTradeUtils.itemsForEmeralds(Items.SUGAR, 26, 1, 16, 10, 0.05F));
            apprenticeTrades.add(VillagerTradeUtils.emeraldsForItems(4, SimpleBBQRegistry.GRILL_BLOCK, 1, 12, 5, 0.05F));
            apprenticeTrades.add(VillagerTradeUtils.emeraldsForItems(4, Items.CAMPFIRE, 1, 12, 5, 0.05F));
        });

        TradeOfferHelper.registerVillagerOffers(SimpleBBQVillagers.SKEWERMAN, 3, journeymanTrades -> {
            journeymanTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.RED_MUSHROOM, 1, 2, SimpleBBQRegistry.CHILI_POWDER, 6, 12, 10, 0.2F));
            journeymanTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.WHEAT_SEEDS, 1, 2, SimpleBBQRegistry.CUMIN, 6, 12, 10, 0.2F));
            journeymanTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.BEETROOT_SEEDS, 1, 2, SimpleBBQRegistry.SALT_AND_PEPPER, 6, 12, 10, 0.2F));
        });

        TradeOfferHelper.registerVillagerOffers(SimpleBBQVillagers.SKEWERMAN, 4, expertTrades -> {
            expertTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.PORKCHOP, 4, 1, SimpleBBQRegistry.PORK_SKEWER, 4, 12, 15, 0.05F));
            expertTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.MUTTON, 4, 1, SimpleBBQRegistry.MUTTON_SKEWER, 4, 12, 15, 0.05F));
            expertTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.BEEF, 3, 1, SimpleBBQRegistry.BEEF_SKEWER, 3, 12, 15, 0.05F));
            expertTrades.add(VillagerTradeUtils.itemsAndEmeraldsToItems(Items.SALMON, 3, 1, SimpleBBQRegistry.SALMON_SKEWER, 3, 12, 15, 0.05F));
        });

        TradeOfferHelper.registerVillagerOffers(SimpleBBQVillagers.SKEWERMAN, 5, masterTrades -> {
            masterTrades.add(VillagerTradeUtils.itemsForEmeralds(Items.BAMBOO, 18, 1, 12, 30, 0.05F));
        });
    }

    public static int onItemUseStart(LivingEntity entity, ItemStack item, int duration) {
        CompoundTag compoundTag = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (ModUtils.hasSeasoning(seasoningList, "chili_powder")) {
                return Math.max(duration - 4, 1);
            }
        }
        return duration;
    }

    public static ItemStack onItemUseFinish(LivingEntity entity, ItemStack item, int duration, ItemStack result) {
        CompoundTag compoundTag = item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (ModUtils.hasSeasoning(seasoningList, "honey")) {
                entity.heal(2.0F);
            }
            if (entity instanceof Player player && !(player instanceof FakePlayer)) {
                var foodProperties = item.getItem().components().get(DataComponents.FOOD);
                if (foodProperties != null) {
                    var foodData = player.getFoodData();
                    var baseNutrition = foodProperties.nutrition();
                    var baseSaturationModifier = foodProperties.saturation();
                    var additionalNutrition = 0;
                    var additionalSaturationModifier = 0.0F;
                    if (ModUtils.hasSeasoning(seasoningList, "salt_and_pepper")) {
                        additionalNutrition += 1;
                    }
                    if (ModUtils.hasSeasoning(seasoningList, "cumin")) {
                        additionalSaturationModifier += 0.1F;
                    }
                    foodData.setFoodLevel(Mth.clamp(foodData.getFoodLevel() + additionalNutrition, 0, 20));
                    foodData.setSaturation(Math.min(foodData.getSaturationLevel() + baseNutrition * additionalSaturationModifier * 2.0F + additionalNutrition * (baseSaturationModifier + additionalSaturationModifier) * 2.0F, (float) foodData.getFoodLevel()));
                }
            }
        }
        return result;
    }

    public static InteractionResult onBlockRightClick(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var pos = hitResult.getBlockPos();
        if (level.getBlockState(pos).getBlock() instanceof SkeweringTableBlock) {
            if (level.getBlockEntity(pos) instanceof SkeweringTableBlockEntity skeweringTableBlockEntity) {
                var stackInHand = player.getItemInHand(hand);
                if (stackInHand.is(SimpleBBQItemTags.SKEWER)) {
                    if (!level.isClientSide() && skeweringTableBlockEntity.skewer(player.getAbilities().instabuild ? stackInHand.copy() : stackInHand, player)) {
                        return InteractionResult.SUCCESS;
                    } else {
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
