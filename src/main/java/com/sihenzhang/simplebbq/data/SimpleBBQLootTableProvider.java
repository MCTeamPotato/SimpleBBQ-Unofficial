package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import io.github.fabricators_of_create.porting_lib.data.ModdedBlockLootSubProvider;
import io.github.fabricators_of_create.porting_lib.data.ModdedLootTableProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class SimpleBBQLootTableProvider extends ModdedLootTableProvider {
    public SimpleBBQLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(SimpleBBQBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    public static class SimpleBBQBlockLoot extends ModdedBlockLootSubProvider {
        public SimpleBBQBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
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
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer) {
            this.generate();
            this.getKnownBlocks().forEach(block -> {
                var table = this.map.remove(block);
                if (table != null) {
                    writer.accept(block.getLootTable(), table);
                }
            });
        }
    }
}
