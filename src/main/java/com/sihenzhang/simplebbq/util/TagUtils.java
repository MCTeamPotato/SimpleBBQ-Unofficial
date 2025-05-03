package com.sihenzhang.simplebbq.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class TagUtils {
    private TagUtils() {
    }

    public static TagKey<Item> createItemTag(String name) {
        return TagKey.create(Registries.ITEM, RLUtils.createRL(name));
    }

    public static TagKey<Item> createItemTag(String modId, String name) {
        return TagKey.create(Registries.ITEM, RLUtils.createRL(modId, name));
    }

    public static TagKey<Item> createConventionItemTag(String name) {
        return TagKey.create(Registries.ITEM, RLUtils.createConventionTagRL(name));
    }

    public static TagKey<Item> createVanillaItemTag(String name) {
        return TagKey.create(Registries.ITEM, RLUtils.createVanillaRL(name));
    }

    public static TagKey<Block> createBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, RLUtils.createRL(name));
    }

    public static TagKey<Block> createBlockTag(String modId, String name) {
        return TagKey.create(Registries.BLOCK, RLUtils.createRL(modId, name));
    }

    public static TagKey<Block> createConventionBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, RLUtils.createConventionTagRL(name));
    }

    public static TagKey<Block> createVanillaBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, RLUtils.createVanillaRL(name));
    }
}
