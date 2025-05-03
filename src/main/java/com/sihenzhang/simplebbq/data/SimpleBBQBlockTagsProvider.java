package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.ResourceKeyUtils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class SimpleBBQBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public SimpleBBQBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ResourceKeyUtils.getBlockResourceKey(SimpleBBQRegistry.GRILL_BLOCK));
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(ResourceKeyUtils.getBlockResourceKey(SimpleBBQRegistry.GRILL_BLOCK));
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ResourceKeyUtils.getBlockResourceKey(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK));
    }

    @Override
    public String getName() {
        return "SimpleBBQ Block Tags";
    }
}
