package com.sihenzhang.simplebbq.integration.recipeviewer_common;

import com.google.common.base.Suppliers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public abstract class AbstractDoubleItemStack {
    protected final Supplier<ItemStack> primarySupplier;
    protected final Supplier<ItemStack> secondarySupplier;

    public AbstractDoubleItemStack(ItemStack primary, ItemStack secondary) {
        this.primarySupplier = Suppliers.memoize(() -> primary);
        this.secondarySupplier = Suppliers.memoize(() -> secondary);
    }

    protected static void renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.renderItem(stack, x, y);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
    }
}
