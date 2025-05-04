package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import org.jetbrains.annotations.NotNull;

public class GrillCookingCategory extends AbstractCookingWithoutFuelAndXpCategory<GrillCookingRecipe> {
    public GrillCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SimpleBBQRegistry.GRILL_BLOCK_ITEM.getDefaultInstance()), "category.grill_cooking", 200);
    }

    @Override
    public @NotNull RecipeType<GrillCookingRecipe> getRecipeType() {
        return SimpleBBQJEIRecipes.GRILL_COOKING;
    }
}
