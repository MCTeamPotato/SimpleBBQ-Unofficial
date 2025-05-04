package com.sihenzhang.simplebbq.integration.rei.category;

import com.google.common.collect.Lists;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.rei.DoubleItemStackRenderer;
import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import com.sihenzhang.simplebbq.integration.rei.display.SeasoningDisplay;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SeasoningCategory implements DisplayCategory<SeasoningDisplay> {
    @Override
    public CategoryIdentifier<? extends SeasoningDisplay> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.SEASONING;
    }

    @Override
    public Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.seasoning");
    }

    @Override
    public Renderer getIcon() {
        return new DoubleItemStackRenderer(
                SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(),
                SimpleBBQRegistry.CHILI_POWDER.get().getDefaultInstance()
        );
    }

    @Override
    public List<Widget> setupDisplay(SeasoningDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 31, bounds.getCenterY() - 13);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4 - 22, startPoint.y + 5)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5)).entries(display.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 48;
    }
}
