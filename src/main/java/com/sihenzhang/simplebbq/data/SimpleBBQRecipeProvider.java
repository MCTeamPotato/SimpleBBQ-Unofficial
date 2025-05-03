package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SeasoningRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SkeweringRecipeBuilder;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class SimpleBBQRecipeProvider extends RecipeProvider {
    public SimpleBBQRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output,registries);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        // crafting recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleBBQRegistry.GRILL_BLOCK)
                .define('#', Blocks.IRON_TRAPDOOR)
                .define('X', ConventionalItemTags.IRON_INGOTS)
                .define('I', ConventionalItemTags.WOODEN_RODS)
                .pattern("X#X")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleBBQRegistry.SKEWERING_TABLE_BLOCK)
                .define('#', ItemTags.PLANKS)
                .define('_', Blocks.SMOOTH_STONE_SLAB)
                .pattern("__")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_stone_slab", has(Blocks.SMOOTH_STONE_SLAB))
                .save(recipeOutput);

        // skewering recipe
        skeweringRecipe(recipeOutput, Items.BEEF, SimpleBBQRegistry.BEEF_SKEWER);
        skeweringRecipe(recipeOutput, Items.CHICKEN, SimpleBBQRegistry.CHICKEN_SKEWER);
        skeweringRecipe(recipeOutput, Items.MUTTON, SimpleBBQRegistry.MUTTON_SKEWER);
        skeweringRecipe(recipeOutput, Items.PORKCHOP, SimpleBBQRegistry.PORK_SKEWER);
        skeweringRecipe(recipeOutput, Items.RABBIT, SimpleBBQRegistry.RABBIT_SKEWER);
        skeweringRecipe(recipeOutput, Items.COD, SimpleBBQRegistry.COD_SKEWER);
        skeweringRecipe(recipeOutput, Items.SALMON, SimpleBBQRegistry.SALMON_SKEWER);
        skeweringRecipe(recipeOutput, Items.BREAD, SimpleBBQRegistry.BREAD_SLICE_SKEWER);
        skeweringRecipe(recipeOutput, Items.BROWN_MUSHROOM, SimpleBBQRegistry.MUSHROOM_SKEWER);
        skeweringRecipe(recipeOutput, Items.POTATO, SimpleBBQRegistry.POTATO_SKEWER);

        // skewer cooking recipe
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.BEEF_SKEWER, SimpleBBQRegistry.COOKED_BEEF_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.CHICKEN_SKEWER, SimpleBBQRegistry.COOKED_CHICKEN_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.MUTTON_SKEWER, SimpleBBQRegistry.COOKED_MUTTON_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.PORK_SKEWER, SimpleBBQRegistry.COOKED_PORK_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.RABBIT_SKEWER, SimpleBBQRegistry.COOKED_RABBIT_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.COD_SKEWER, SimpleBBQRegistry.COOKED_COD_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.SALMON_SKEWER, SimpleBBQRegistry.COOKED_SALMON_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.BREAD_SLICE_SKEWER, SimpleBBQRegistry.TOAST_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.MUSHROOM_SKEWER, SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER, 300);
        grillCookingRecipe(recipeOutput, SimpleBBQRegistry.POTATO_SKEWER, SimpleBBQRegistry.BAKED_POTATO_SKEWER, 300);

        // seasoning recipe
        seasoningRecipe(recipeOutput, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_HONEY), Items.HONEY_BOTTLE, "honey");
        seasoningRecipe(recipeOutput, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CHILI_POWDER), SimpleBBQRegistry.CHILI_POWDER);
        seasoningRecipe(recipeOutput, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CUMIN), SimpleBBQRegistry.CUMIN);
        seasoningRecipe(recipeOutput, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_SALT_AND_PEPPER), SimpleBBQRegistry.SALT_AND_PEPPER);
    }

    protected static void grillCookingRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, Item pResult, int pCookingTime) {
        grillCookingRecipe(recipeOutput, pIngredient, pResult.getDefaultInstance(), pCookingTime);
    }
    protected static void grillCookingRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, ItemStack pResult, int pCookingTime) {
        GrillCookingRecipeBuilder.cooking(Ingredient.of(pIngredient), pResult, pCookingTime).group("grill").save(recipeOutput, getSimpleRecipeName("grill_cooking", pResult.getItem()));
    }

    protected static void skeweringRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, int pIngredientCount, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult, pResultCount).save(recipeOutput, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, int pIngredientCount, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult).save(recipeOutput, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult, pResultCount).save(recipeOutput, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult).save(recipeOutput, getSimpleRecipeName("skewering", pResult));
    }

    protected static void seasoningRecipe(RecipeOutput recipeOutput, Ingredient pIngredient, ItemLike pSeasoning) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), getItemName(pSeasoning)).unlockedBy("has_ingredient",has(pSeasoning)).save(recipeOutput, getSimpleRecipeName("seasoning", getItemName(pSeasoning)));
    }

    protected static void seasoningRecipe(RecipeOutput recipeOutput, Ingredient pIngredient, ItemLike pSeasoning, String name) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), name).unlockedBy("has_ingredient",has(pSeasoning)).save(recipeOutput, getSimpleRecipeName("seasoning", name));
    }

    public static String getSimpleRecipeName(ItemLike pItemLike) {
        return getSimpleRecipeName(getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String name) {
        return SimpleBBQ.MOD_ID + ":" + name;
    }

    protected static String getSimpleRecipeName(String pRecipeType, ItemLike pItemLike) {
        return getSimpleRecipeName(pRecipeType, getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String pRecipeType, String name) {
        return SimpleBBQ.MOD_ID + ":" + pRecipeType + "/" + name;
    }
}
