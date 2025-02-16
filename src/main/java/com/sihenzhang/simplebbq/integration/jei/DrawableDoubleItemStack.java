package com.sihenzhang.simplebbq.integration.jei;

import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import mezz.jei.api.gui.drawable.IDrawable;

import java.util.function.Supplier;

public class DrawableDoubleItemStack implements IDrawable {
    private final Supplier<ItemStack> primarySupplier;
    private final Supplier<ItemStack> secondarySupplier;

    public DrawableDoubleItemStack(ItemStack primary, ItemStack secondary) {
        this.primarySupplier = Suppliers.memoize(() -> primary);
        this.secondarySupplier = Suppliers.memoize(() -> secondary);
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        var primaryStack = primarySupplier.get();
        var secondaryStack = secondarySupplier.get();
        
        if (primaryStack != null && !primaryStack.isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(xOffset + 1, yOffset + 1, 0);
            renderItemStack(guiGraphics, primaryStack, 0, 0);
            guiGraphics.pose().popPose();
        }
        
        if (secondaryStack != null && !secondaryStack.isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(xOffset + 8, yOffset + 8, 100);
            guiGraphics.pose().scale(0.6F, 0.6F, 0.6F);
            renderItemStack(guiGraphics, secondaryStack, 0, 0);
            guiGraphics.pose().popPose();
        }
    }

    private static void renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.renderItem(stack, x, y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
    }
}
