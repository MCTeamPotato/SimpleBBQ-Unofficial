package com.sihenzhang.simplebbq.integration.recipeviewer_common;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQConfig;
import com.sihenzhang.simplebbq.util.I18nUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeViewerHelper {
    public static int getCampfireCookingOnGrillTime(int cookingTime) {
        return Mth.clamp((int) (cookingTime * SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_COOKING_TIME_MODIFIER.get()), Math.min(SimpleBBQConfig.CAMPFIRE_COOKING_ON_GRILL_MINIMUM_COOKING_TIME.get(), cookingTime), cookingTime);
    }

    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }
        RegistryAccess registryAccess = level.registryAccess();
        return recipe.getResultItem(registryAccess);
    }

    public static MutableComponent createRecipeViewerComponent(String suffix) {
        return I18nUtils.createComponent("recipeViewer", SimpleBBQ.MOD_ID, suffix);
    }

    public static MutableComponent createRecipeViewerComponent(String suffix, Object... args) {
        return I18nUtils.createComponent("recipeViewer", SimpleBBQ.MOD_ID, suffix, args);
    }
}
