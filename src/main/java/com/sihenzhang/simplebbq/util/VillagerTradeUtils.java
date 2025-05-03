package com.sihenzhang.simplebbq.util;

import com.sihenzhang.simplebbq.thirdparty.util.BasicItemListing;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class VillagerTradeUtils {
    public static VillagerTrades.ItemListing emeraldsForItems(int emeraldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(emeraldCost, new ItemStack(toItem, toCount), maxUses, villagerXp, priceMultiplier);
    }

    public static VillagerTrades.ItemListing itemsAndEmeraldsToItems(ItemLike fromItem, int fromCount, int emeraldCost, ItemLike toItem, int toCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(new ItemStack(Items.EMERALD, emeraldCost), new ItemStack(fromItem, fromCount), new ItemStack(toItem, toCount), maxUses, villagerXp, priceMultiplier);
    }

    public static VillagerTrades.ItemListing itemsForEmeralds(ItemLike fromItem, int fromCount, int emeraldCount, int maxUses, int villagerXp, float priceMultiplier) {
        return new BasicItemListing(new ItemStack(fromItem, fromCount), new ItemStack(Items.EMERALD, emeraldCount), maxUses, villagerXp, priceMultiplier);
    }
}
