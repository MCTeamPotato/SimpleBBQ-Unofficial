package com.sihenzhang.simplebbq.integration.jei.category;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.jei.DrawableDoubleItemStack;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIRecipes;
import com.sihenzhang.simplebbq.integration.jei.simple.AbstractCampfireCookingCategory;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import org.jetbrains.annotations.NotNull;

public class CampfireCookingOnGrillCategory extends AbstractCampfireCookingCategory<CampfireCookingRecipe> {
    public CampfireCookingOnGrillCategory(IGuiHelper guiHelper) {
        super(guiHelper, 400);
    }

    @Override
    public RecipeType<CampfireCookingRecipe> getRecipeType() {
        return SimpleBBQJEIRecipes.CAMPFIRE_COOKING_ON_GRILL;
    }

    @Override
    public @NotNull Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.campfire_cooking_on_grill");
    }

    @Override
    public IDrawable getIcon() {
        return new DrawableDoubleItemStack(
                SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(),
                Items.CAMPFIRE.getDefaultInstance()
        );
    }

    @Override
    protected IDrawableAnimated getArrow(int cookingTime) {
        return super.getArrow(RecipeViewerHelper.getCampfireCookingOnGrillTime(cookingTime));
    }

    @Override
    protected void drawCookingTime(int cookingTime, GuiGraphics stack) {
        super.drawCookingTime(RecipeViewerHelper.getCampfireCookingOnGrillTime(cookingTime), stack);
    }
}
