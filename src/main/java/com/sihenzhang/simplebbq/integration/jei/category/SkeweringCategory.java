package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIPlugin;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.integration.jei.simple.SimpleRecipeCategory;
import com.sihenzhang.simplebbq.recipe.SkeweringRecipe;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class SkeweringCategory extends SimpleRecipeCategory<SkeweringRecipe> {
    private IDrawable icon;

    public SkeweringCategory(IGuiHelper guiHelper) {
        super(guiHelper.drawableBuilder(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 0, 168, 125, 18).build());
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                SimpleBBQRegistry.SKEWERING_TABLE_BLOCK_ITEM.get().getDefaultInstance()
        );
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
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SkeweringRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(Ingredient.of(SimpleBBQItemTags.SKEWER));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(RecipeViewerHelper.getResultItem(recipe));
    }
}
