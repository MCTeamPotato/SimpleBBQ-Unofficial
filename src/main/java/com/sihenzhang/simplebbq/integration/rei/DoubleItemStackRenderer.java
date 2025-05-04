package com.sihenzhang.simplebbq.integration.rei;

import com.sihenzhang.simplebbq.integration.recipeviewer_common.AbstractDoubleItemStack;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class DoubleItemStackRenderer extends AbstractDoubleItemStack implements Renderer {

    public DoubleItemStackRenderer(ItemStack primary, ItemStack secondary) {
        super(primary, secondary);
    }

    @Override
    public void render(GuiGraphics guiGraphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
        var primaryStack = primarySupplier.get();
        var secondaryStack = secondarySupplier.get();

        if (primaryStack != null && !primaryStack.isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(bounds.getCenterX() - 8, bounds.getCenterY() - 8, 0);
            renderItemStack(guiGraphics, primaryStack, 0, 0);
            guiGraphics.pose().popPose();
        }

        if (secondaryStack != null && !secondaryStack.isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(bounds.getCenterX() - 1, bounds.getCenterY() - 1, 100);
            guiGraphics.pose().scale(0.6F, 0.6F, 0.6F);
            renderItemStack(guiGraphics, secondaryStack, 0, 0);
            guiGraphics.pose().popPose();
        }
    }
}
