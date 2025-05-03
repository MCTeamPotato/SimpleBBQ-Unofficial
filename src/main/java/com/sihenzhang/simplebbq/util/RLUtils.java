package com.sihenzhang.simplebbq.util;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.resources.ResourceLocation;

public final class RLUtils {
    private RLUtils() {
    }

    public static ResourceLocation createRL(String path) {
        return createRL(SimpleBBQ.MOD_ID, path);
    }

    public static ResourceLocation createRL(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation createConventionTagRL(String path) {
        return createRL("c", path);
    }

    public static ResourceLocation createVanillaRL(String path) {
        return createRL("minecraft", path);
    }
}
