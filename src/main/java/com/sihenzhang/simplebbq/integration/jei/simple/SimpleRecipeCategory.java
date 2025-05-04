package com.sihenzhang.simplebbq.integration.jei.simple;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;

public abstract class SimpleRecipeCategory<T> implements IRecipeCategory<T> {
    protected final IDrawable background;

    protected SimpleRecipeCategory(IDrawable background) {
        this.background = background;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }
}
