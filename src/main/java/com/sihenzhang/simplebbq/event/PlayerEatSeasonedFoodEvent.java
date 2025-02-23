package com.sihenzhang.simplebbq.event;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = SimpleBBQ.MOD_ID)
public class PlayerEatSeasonedFoodEvent {
    @SubscribeEvent
    public static void onItemUseStart(final LivingEntityUseItemEvent.Start event) {
        var stack = event.getItem();
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (hasSeasoning(seasoningList, "chili_powder")) {
                event.setDuration(Math.max(event.getDuration() - 4, 1));
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(final LivingEntityUseItemEvent.Finish event) {
        var stack = event.getItem();
        CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.getBoolean("HasEffect") && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            if (hasSeasoning(seasoningList, "honey")) {
                event.getEntity().heal(2.0F);
            }
            if (event.getEntity() instanceof Player player && !(player instanceof FakePlayer)) {
                var foodProperties = stack.getFoodProperties(player);
                if (foodProperties != null) {
                    var foodData = player.getFoodData();
                    var baseNutrition = foodProperties.nutrition();
                    var baseSaturationModifier = foodProperties.saturation();
                    var additionalNutrition = 0;
                    var additionalSaturationModifier = 0.0F;
                    if (hasSeasoning(seasoningList, "salt_and_pepper")) {
                        additionalNutrition += 1;
                    }
                    if (hasSeasoning(seasoningList, "cumin")) {
                        additionalSaturationModifier += 0.1F;
                    }
                    foodData.setFoodLevel(Mth.clamp(foodData.getFoodLevel() + additionalNutrition, 0, 20));
                    foodData.setSaturation(Math.min(foodData.getSaturationLevel() + baseNutrition * additionalSaturationModifier * 2.0F + additionalNutrition * (baseSaturationModifier + additionalSaturationModifier) * 2.0F, (float) foodData.getFoodLevel()));
                }
            }
        }
    }

    private static boolean hasSeasoning(ListTag seasoning, String name) {
        return seasoning.stream().filter(tag -> tag.getId() == Tag.TAG_STRING).map(StringTag.class::cast).anyMatch(tag -> tag.getAsString().equalsIgnoreCase(name));
    }
}
