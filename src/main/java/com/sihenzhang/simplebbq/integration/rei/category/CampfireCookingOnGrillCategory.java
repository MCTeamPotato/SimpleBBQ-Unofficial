package com.sihenzhang.simplebbq.integration.rei.category;

import com.google.common.collect.Lists;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.integration.rei.DoubleItemStackRenderer;
import com.sihenzhang.simplebbq.integration.rei.SimpleBBQREIRecipes;
import com.sihenzhang.simplebbq.integration.rei.display.CampfireCookingOnGrillDisplay;
import com.sihenzhang.simplebbq.integration.recipeviewer_common.RecipeViewerHelper;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.text.DecimalFormat;
import java.util.List;

public class CampfireCookingOnGrillCategory implements DisplayCategory<CampfireCookingOnGrillDisplay> {
    @Override
    public CategoryIdentifier<? extends CampfireCookingOnGrillDisplay> getCategoryIdentifier() {
        return SimpleBBQREIRecipes.CAMPFIRE_COOKING_ON_GRILL;
    }

    @Override
    public Component getTitle() {
        return RecipeViewerHelper.createRecipeViewerComponent("category.campfire_cooking_on_grill");
    }

    @Override
    public Renderer getIcon() {
        return new DoubleItemStackRenderer(
                SimpleBBQRegistry.GRILL_BLOCK_ITEM.get().getDefaultInstance(),
                Items.CAMPFIRE.getDefaultInstance()
        );
    }

    @Override
    public List<Widget> setupDisplay(CampfireCookingOnGrillDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.y + 10);
        final double cookingTime = RecipeViewerHelper.getCampfireCookingOnGrillTime((int) display.getCookTime());
        DecimalFormat df = new DecimalFormat("###.##");
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 9)));
        widgets.add(Widgets.createBurningFire(new Point(startPoint.x + 1, startPoint.y + 20)).animationDurationMS(10000));
        widgets.add(Widgets.createLabel(new Point(bounds.x + bounds.width - 5, bounds.y + 5),
                Component.translatable("category.rei.campfire.time", df.format(cookingTime / 20d))).noShadow().rightAligned().color(0xFF404040, 0xFFBBBBBB));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 24, startPoint.y + 8)).animationDurationTicks(cookingTime));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 1, startPoint.y + 1)).entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 9)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 49;
    }
}
