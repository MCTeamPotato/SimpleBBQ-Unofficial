package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class SimpleBBQLootTableProvider extends LootTableProvider {
    public SimpleBBQLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(SimpleBBQBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    public static class SimpleBBQBlockLoot extends BlockLootSubProvider {
        public SimpleBBQBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.dropSelf(SimpleBBQRegistry.GRILL_BLOCK.get());
            this.dropSelf(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return SimpleBBQRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
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
