package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIPlugin;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

public class SkeweringCategory extends BaseCategory<SkeweringRecipe> {
    public SkeweringCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build()
        ,guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.getDefaultInstance()));
    }


    @Override
    public RecipeType<SkeweringRecipe> getRecipeType() {
        return SimpleBBQJEIRecipes.SKEWERING;
    }

    @Override
    public Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.skewering");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SkeweringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(Ingredient.of(SimpleBBQItemTags.SKEWER));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(RecipeUtil.getResultItem(recipe));
    }
}
