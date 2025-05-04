package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.integration.jei.simple.AbstractCampfireCookingCategory;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import com.sihenzhang.simplebbq.recipe.GrillCookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GrillCookingCategory extends AbstractCampfireCookingCategory<GrillCookingRecipe> {
    private IDrawable icon;

    public GrillCookingCategory(IGuiHelper guiHelper) {
        super(guiHelper, 200);
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                SimpleBBQRegistry.GRILL_BLOCK_ITEM.getDefaultInstance()
        );
    }

    @Override
    public @NotNull RecipeType<GrillCookingRecipe> getRecipeType() {
        return SimpleBBQJEIRecipes.GRILL_COOKING;
    }

    @Override
    public @NotNull Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.grill_cooking");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }
}
