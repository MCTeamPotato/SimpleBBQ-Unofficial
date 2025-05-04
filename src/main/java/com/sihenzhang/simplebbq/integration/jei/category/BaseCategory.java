package com.sihenzhang.simplebbq.integration.jei.category;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;

public abstract class BaseCategory<T> implements IRecipeCategory<T> {

    protected final IDrawable background;
    protected final IDrawable icon;

    protected BaseCategory(IDrawable background, IDrawable icon) {
        this.background = background;
        this.icon = icon;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
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
