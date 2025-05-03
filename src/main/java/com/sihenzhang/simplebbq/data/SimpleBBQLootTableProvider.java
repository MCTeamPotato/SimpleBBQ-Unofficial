package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import io.github.fabricators_of_create.porting_lib.data.ModdedBlockLootSubProvider;
import io.github.fabricators_of_create.porting_lib.data.ModdedLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SimpleBBQLootTableProvider extends ModdedLootTableProvider {
    public SimpleBBQLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(SimpleBBQBlockLoot::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    public static class SimpleBBQBlockLoot extends ModdedBlockLootSubProvider {
        public SimpleBBQBlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(),registries);
        }

        @Override
        public void generate() {
            this.dropSelf(SimpleBBQRegistry.GRILL_BLOCK);
            this.dropSelf(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return SimpleBBQRegistry.BLOCKS.values().stream().toList();
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
