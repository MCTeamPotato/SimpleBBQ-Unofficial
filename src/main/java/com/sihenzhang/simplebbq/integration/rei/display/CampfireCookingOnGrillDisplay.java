package com.sihenzhang.simplebbq.integration.rei.display;

import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.plugin.common.displays.DefaultCampfireDisplay;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;

public class CampfireCookingOnGrillDisplay extends DefaultCampfireDisplay {
    public CampfireCookingOnGrillDisplay(CampfireCookingRecipe recipe) {
        super(recipe);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.CAMPFIRE_COOKING_ON_GRILL;
    }
}
