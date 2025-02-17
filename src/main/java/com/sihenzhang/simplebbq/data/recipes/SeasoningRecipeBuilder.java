package com.sihenzhang.simplebbq.data.recipes;

import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SeasoningRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final Ingredient seasoning;
    private final String name;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SeasoningRecipeBuilder(Ingredient ingredient, Ingredient seasoning, String name) {
        this.ingredient = ingredient;
        this.seasoning = seasoning;
        this.name = name;
    }

    public static SeasoningRecipeBuilder seasoning(Ingredient ingredient, Ingredient seasoning, String name) {
        return new SeasoningRecipeBuilder(ingredient, seasoning, name);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation pRecipeId) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        recipeOutput.accept(pRecipeId,new SeasoningRecipe(ingredient,seasoning,name),advancement.build(pRecipeId.withPrefix("recipes/")));
    }

}
