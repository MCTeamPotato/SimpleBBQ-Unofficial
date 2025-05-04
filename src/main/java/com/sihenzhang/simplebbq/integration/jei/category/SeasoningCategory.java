package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.DrawableDoubleItemStack;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIPlugin;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.recipe.SeasoningRecipe;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeasoningCategory extends BaseCategory<SeasoningRecipe> {
    public SeasoningCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build()
        ,new DrawableDoubleItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM.getDefaultInstance(), SimpleBBQRegistry.CHILI_POWDER.getDefaultInstance()));
    }


    @Override
    public RecipeType<SeasoningRecipe> getRecipeType() {
        return SimpleBBQJEIRecipes.SEASONING;
    }

    @Override
    public Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.seasoning");
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, SeasoningRecipe recipe, @NotNull IFocusGroup focuses) {
        var inputItems = List.of(recipe.getIngredient().getItems());
        if (inputItems.stream().anyMatch(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.OUTPUT).anyMatch(focus -> stack.is(focus.getTypedValue().getIngredient().getItem())))) {
            inputItems = inputItems.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.OUTPUT).anyMatch(focus -> stack.is(focus.getTypedValue().getIngredient().getItem()))).toList();
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(inputItems);
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(recipe.getSeasoning());
        var resultItems = recipe.getCachedResultItems().getUnchecked(recipe);
        if (resultItems.stream().anyMatch(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT).anyMatch(focus -> stack.is(focus.getTypedValue().getIngredient().getItem())))) {
            resultItems = resultItems.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, RecipeIngredientRole.INPUT).anyMatch(focus -> stack.is(focus.getTypedValue().getIngredient().getItem()))).toList();
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStacks(resultItems);
    }
}
