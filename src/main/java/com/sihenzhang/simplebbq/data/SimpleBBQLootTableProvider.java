package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class SimpleBBQLootTableProvider extends LootTableProvider {
    public SimpleBBQLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(SimpleBBQBlockLoot::new, LootContextParamSets.BLOCK)
        ),registries);
    }

    public static class SimpleBBQBlockLoot extends BlockLootSubProvider {
        public SimpleBBQBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(),registries);
        }

        @Override
        protected void generate() {
            this.dropSelf(SimpleBBQRegistry.GRILL_BLOCK.get());
            this.dropSelf(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return SimpleBBQRegistry.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
            this.generate();
            this.getKnownBlocks().forEach(block -> {
                var table = this.map.remove(block);
                if (table != null) {
                    output.accept(block.getLootTable(), table);
                }
            });
        }
    }
}
