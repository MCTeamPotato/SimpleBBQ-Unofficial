package com.sihenzhang.simplebbq.integration.jei.simple;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sihenzhang.simplebbq.integration.jei.SimpleBBQJEIPlugin;
import com.sihenzhang.simplebbq.util.I18nUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCampfireCookingCategory<T extends AbstractCookingRecipe> extends SimpleRecipeCategory<T> {
    private final IDrawableAnimated animatedFlame;
    private final int defaultCookingTime;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;

    public AbstractCampfireCookingCategory(IGuiHelper guiHelper, int defaultCookingTime) {
        super(guiHelper.drawableBuilder(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 0, 186, 82, 34).addPadding(0, 10, 0, 0).build());
        this.animatedFlame = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 82, 114, 14, 14), 300, IDrawableAnimated.StartDirection.TOP, true);

        this.defaultCookingTime = defaultCookingTime;
        this.cachedArrows = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<>() {
            @Override
            public IDrawableAnimated load(@NotNull Integer cookingTime) {
                return guiHelper.drawableBuilder(SimpleBBQJEIPlugin.RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(cookingTime, IDrawableAnimated.StartDirection.LEFT, false);
            }
        });
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        animatedFlame.draw(guiGraphics, 1, 20);

        var cookingTime = recipe.getCookingTime();

        this.getArrow(cookingTime).draw(guiGraphics, 24, 8);

        drawCookingTime(cookingTime, guiGraphics);
    }

    protected IDrawableAnimated getArrow(int cookingTime) {
        if (cookingTime <= 0) {
            cookingTime = defaultCookingTime;
        }
        return this.cachedArrows.getUnchecked(cookingTime);
    }

    protected void drawCookingTime(int cookingTime, GuiGraphics guiGraphics) {
        if (cookingTime > 0) {
            var cookingTimeSeconds = cookingTime / 20;
            var timeText = I18nUtils.createComponent("gui", SimpleBBQJEIPlugin.MOD_ID, "category.smelting.time.seconds", cookingTimeSeconds);
            var fontRenderer = Minecraft.getInstance().font;
            var stringWidth = fontRenderer.width(timeText);
            guiGraphics.drawString(fontRenderer, timeText, background.getWidth() - stringWidth, 35, 0xFF808080, false);
        }
    }
}
