package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.data.recipes.GrillCookingRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SeasoningRecipeBuilder;
import com.sihenzhang.simplebbq.data.recipes.SkeweringRecipeBuilder;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleBBQRecipeProvider extends FabricRecipeProvider {
    public SimpleBBQRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        // crafting recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleBBQRegistry.GRILL_BLOCK)
                .define('#', Blocks.IRON_TRAPDOOR)
                .define('X', ConventionalItemTags.IRON_INGOTS)
                .define('I', SimpleBBQItemTags.WOOD_STICK)
                .pattern("X#X")
                .pattern("I I")
                .pattern("I I")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(pFinishedRecipeConsumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SimpleBBQRegistry.SKEWERING_TABLE_BLOCK)
                .define('#', ItemTags.PLANKS)
                .define('_', Blocks.SMOOTH_STONE_SLAB)
                .pattern("__")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_stone_slab", has(Blocks.SMOOTH_STONE_SLAB))
                .save(pFinishedRecipeConsumer);

        // skewering recipe
        skeweringRecipe(pFinishedRecipeConsumer, Items.BEEF, SimpleBBQRegistry.BEEF_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.CHICKEN, SimpleBBQRegistry.CHICKEN_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.MUTTON, SimpleBBQRegistry.MUTTON_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.PORKCHOP, SimpleBBQRegistry.PORK_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.RABBIT, SimpleBBQRegistry.RABBIT_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.COD, SimpleBBQRegistry.COD_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.SALMON, SimpleBBQRegistry.SALMON_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.BREAD, SimpleBBQRegistry.BREAD_SLICE_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.BROWN_MUSHROOM, SimpleBBQRegistry.MUSHROOM_SKEWER);
        skeweringRecipe(pFinishedRecipeConsumer, Items.POTATO, SimpleBBQRegistry.POTATO_SKEWER);

        // skewer cooking recipe
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.BEEF_SKEWER, SimpleBBQRegistry.COOKED_BEEF_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.CHICKEN_SKEWER, SimpleBBQRegistry.COOKED_CHICKEN_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.MUTTON_SKEWER, SimpleBBQRegistry.COOKED_MUTTON_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.PORK_SKEWER, SimpleBBQRegistry.COOKED_PORK_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.RABBIT_SKEWER, SimpleBBQRegistry.COOKED_RABBIT_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.COD_SKEWER, SimpleBBQRegistry.COOKED_COD_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.SALMON_SKEWER, SimpleBBQRegistry.COOKED_SALMON_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.BREAD_SLICE_SKEWER, SimpleBBQRegistry.TOAST_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.MUSHROOM_SKEWER, SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER, 300);
        grillCookingRecipe(pFinishedRecipeConsumer, SimpleBBQRegistry.POTATO_SKEWER, SimpleBBQRegistry.BAKED_POTATO_SKEWER, 300);

        // seasoning recipe
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_HONEY), Items.HONEY_BOTTLE, "honey");
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CHILI_POWDER), SimpleBBQRegistry.CHILI_POWDER);
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CUMIN), SimpleBBQRegistry.CUMIN);
        seasoningRecipe(pFinishedRecipeConsumer, Ingredient.of(SimpleBBQItemTags.CAN_BE_SEASONED_BY_SALT_AND_PEPPER), SimpleBBQRegistry.SALT_AND_PEPPER);
    }

    protected static void grillCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pCookingTime) {
        GrillCookingRecipeBuilder.cooking(Ingredient.of(pIngredient), pResult, pCookingTime).save(pFinishedRecipeConsumer, getSimpleRecipeName("grill_cooking", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, int pIngredientCount, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult, pResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, int pIngredientCount, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pIngredientCount, pResult).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pResultCount) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult, pResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void skeweringRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult) {
        SkeweringRecipeBuilder.skewering(Ingredient.of(pIngredient), pResult).save(pFinishedRecipeConsumer, getSimpleRecipeName("skewering", pResult));
    }

    protected static void seasoningRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Ingredient pIngredient, ItemLike pSeasoning) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), getItemName(pSeasoning)).save(pFinishedRecipeConsumer, getSimpleRecipeName("seasoning", getItemName(pSeasoning)));
    }

    protected static void seasoningRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Ingredient pIngredient, ItemLike pSeasoning, String name) {
        SeasoningRecipeBuilder.seasoning(pIngredient, Ingredient.of(pSeasoning), name).save(pFinishedRecipeConsumer, getSimpleRecipeName("seasoning", name));
    }

    public static @NotNull String getSimpleRecipeName(ItemLike pItemLike) {
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
