package com.sihenzhang.simplebbq.thirdparty.transfer;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandlerContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemTransferHelper {
    public static ItemStack insertItem(ItemStackHandlerContainer container, int slot, @NotNull ItemStack stack, boolean simulate) {
        if (container.indexInvalid(slot))
            return stack;

        ItemStack existing = container.getItem(slot);
        int limit = Math.min(container.getSlotLimit(slot), stack.getMaxStackSize());

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        ItemStack setStack;
        if (existing.isEmpty()) {
            setStack = stack;
        } else {
            // TODO: Figure out if the game will freeze upon setting the item to the already existing item.
            setStack = existing;
            setStack.grow(reachedLimit ? limit : stack.getCount());
        }
        // It is required to set the stack for syncing purposes.
        container.setItem(slot, setStack);

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    public static ItemStack extractItem(ItemStackHandlerContainer container, int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(container, slot);

        ItemStack existing = container.getItem(slot);

        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
//            if (!simulate) {
//                container.setItem(slot, ItemStack.EMPTY);
//                return existing;
//            } else {
//                return existing.copy();
//            }
            container.setItem(slot, ItemStack.EMPTY);
            return existing;
        } else {
//            if (!simulate) {
//                container.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
//            }
            container.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    protected static void validateSlotIndex(ItemStackHandlerContainer container, int slot) {
        if (slot < 0 || slot >= container.getSlots().size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + container.getSlots().size() + ")");
    }
}
