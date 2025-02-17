package com.sihenzhang.simplebbq.data.recipes;

import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class GrillCookingRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final Ingredient ingredient;
    private final int cookingTime;
    @Nullable
    private String group;

    public GrillCookingRecipeBuilder(ItemStack result, Ingredient ingredient, int cookingTime) {
        this.result = result;
        this.ingredient = ingredient;
        this.cookingTime = cookingTime;
    }

    public static GrillCookingRecipeBuilder cooking(Ingredient ingredient, ItemStack result, int cookingTime) {
        return new GrillCookingRecipeBuilder(result, ingredient, cookingTime);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        group = pGroupName;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation pRecipeId) {
        GrillCookingRecipe recipe = new GrillCookingRecipe(group, ingredient, result, cookingTime);
        recipeOutput.accept(pRecipeId, recipe,null);
    }

}
