package com.sihenzhang.simplebbq.integration.jei;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import org.jetbrains.annotations.NotNull;

public class GrillCookingCategory extends AbstractCookingWithoutFuelAndXpCategory<GrillCookingRecipe> {
    public static final RecipeType<GrillCookingRecipe> RECIPE_TYPE = RecipeType.create(SimpleBBQ.MOD_ID, "grill_cooking", GrillCookingRecipe.class);

    public GrillCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SimpleBBQRegistry.GRILL_BLOCK_ITEM.getDefaultInstance()), "category.grill_cooking", 200);
    }

    @Override
    public @NotNull RecipeType<GrillCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }
}
