package com.sihenzhang.simplebbq.integration.jei;

import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.util.RLUtils;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;

public class SimpleBBQJEIRecipes {
    public static final RecipeType<SkeweringRecipe> SKEWERING = createRecipeType("skewering", SkeweringRecipe.class);
    public static final RecipeType<SeasoningRecipe> SEASONING = createRecipeType("seasoning", SeasoningRecipe.class);
    public static final RecipeType<GrillCookingRecipe> GRILL_COOKING = createRecipeType("grill_cooking", GrillCookingRecipe.class);
    public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING_ON_GRILL = createRecipeType("campfire_cooking_on_grill", CampfireCookingRecipe.class);

    protected static <T> RecipeType<T> createRecipeType( String path, Class<? extends T> recipeClass) {
        return new RecipeType<>(RLUtils.createRL(path), recipeClass);
    }
}
