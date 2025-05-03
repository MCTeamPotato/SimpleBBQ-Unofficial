package com.sihenzhang.simplebbq.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class ModUtils {
    public static boolean hasSeasoning(ListTag seasoning, String name) {
        return seasoning.stream().filter(tag -> tag.getId() == Tag.TAG_STRING).map(StringTag.class::cast).anyMatch(tag -> tag.getAsString().equalsIgnoreCase(name));
    }
}
