package com.sihenzhang.simplebbq.data.recipes;

import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class SkeweringRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final Ingredient ingredient;
    private final int ingredientCount;
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    public SkeweringRecipeBuilder(ItemLike result, int resultCount, Ingredient ingredient, int ingredientCount) {
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.ingredient = ingredient;
        this.ingredientCount = ingredientCount;
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, int ingredientCount, ItemLike result, int resultCount) {
        return new SkeweringRecipeBuilder(result, resultCount, ingredient, ingredientCount);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, int ingredientCount, ItemLike result) {
        return skewering(ingredient, ingredientCount, result, 1);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, ItemLike result, int resultCount) {
        return skewering(ingredient, 1, result, resultCount);
    }

    public static SkeweringRecipeBuilder skewering(Ingredient ingredient, ItemLike result) {
        return skewering(ingredient, 1, result, 1);
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
        return result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation pRecipeId) {
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        recipeOutput.accept(pRecipeId,new SkeweringRecipe(ingredient,resultCount,result.getDefaultInstance()),advancement.build(pRecipeId.withPrefix("recipes/")));
    }

}
