package com.sihenzhang.simplebbq;


import net.neoforged.neoforge.common.ModConfigSpec;

public class SimpleBBQConfig {
    public static ModConfigSpec COMMON_CONFIG;

    public static ModConfigSpec.IntValue CAMPFIRE_COOKING_ON_GRILL_MINIMUM_COOKING_TIME;
    public static ModConfigSpec.DoubleValue CAMPFIRE_COOKING_ON_GRILL_COOKING_TIME_MODIFIER;

    static {
        final var commonBuilder = new ModConfigSpec.Builder();

        commonBuilder.comment("General settings").push("general");
        CAMPFIRE_COOKING_ON_GRILL_MINIMUM_COOKING_TIME = commonBuilder
                .comment("Set this value to change the minimum cooking time for the Campfire Cooking Recipe when cooking on the grill.\nNote that this value will not be greater than the base cooking time, so even if a large value is set, it will still be equal to the base cooking time.")
                .defineInRange("campfireCookingOnGrillMinimumCookingTime", 200, 1, Integer.MAX_VALUE);
        CAMPFIRE_COOKING_ON_GRILL_COOKING_TIME_MODIFIER = commonBuilder
                .comment("Set this value to change the cooking time modifier for the Campfire Cooking Recipe when cooking on the grill.\nactualCookingTime = min(baseCookingTime * campfireCookingOnGrillSpeedModifier, campfireCookingOnGrillMinimumCookingTime)")
                .defineInRange("campfireCookingOnGrillCookingTimeModifier", 0.5D, 0.0D, 1.0D);
        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();
    }
}
