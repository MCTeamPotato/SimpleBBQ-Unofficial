package com.sihenzhang.simplebbq.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public class SeasoningInput implements RecipeInput {

    private final List<ItemStack> items;

    public SeasoningInput(ItemStack... items) {
        this.items = List.of(items);
    }
    public SeasoningInput(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public ItemStack getItem(int i) {
        return items.get(i);
    }

    @Override
    public int size() {
        return items.size();
    }
}
