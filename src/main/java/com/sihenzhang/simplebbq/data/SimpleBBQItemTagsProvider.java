package com.sihenzhang.simplebbq.data;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.tag.SimpleBBQItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class SimpleBBQItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public SimpleBBQItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagProvider blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(SimpleBBQItemTags.SKEWER)
                .addTag(ConventionalItemTags.WOODEN_RODS)
                .add(Items.BAMBOO);
        this.getOrCreateTagBuilder(SimpleBBQItemTags.SEASONING)
                .add(
                        Items.HONEY_BOTTLE,
                        SimpleBBQRegistry.CHILI_POWDER,
                        SimpleBBQRegistry.CUMIN,
                        SimpleBBQRegistry.SALT_AND_PEPPER
                );

        var allRawSkewers = ImmutableSet.of(
                SimpleBBQRegistry.BEEF_SKEWER,
                SimpleBBQRegistry.CHICKEN_SKEWER,
                SimpleBBQRegistry.MUTTON_SKEWER,
                SimpleBBQRegistry.PORK_SKEWER,
                SimpleBBQRegistry.RABBIT_SKEWER,
                SimpleBBQRegistry.COD_SKEWER,
                SimpleBBQRegistry.SALMON_SKEWER,
                SimpleBBQRegistry.BREAD_SLICE_SKEWER,
                SimpleBBQRegistry.MUSHROOM_SKEWER,
                SimpleBBQRegistry.POTATO_SKEWER
        );
        var cannotBeSeasonedByHoney = ImmutableSet.of(
                SimpleBBQRegistry.COD_SKEWER,
                SimpleBBQRegistry.SALMON_SKEWER
        );
        var cannotBeSeasonedByChiliPowder = ImmutableSet.of(SimpleBBQRegistry.BREAD_SLICE_SKEWER);
        var cannotBeSeasonedByCumin = ImmutableSet.of(SimpleBBQRegistry.BREAD_SLICE_SKEWER);
        var cannotBeSeasonedBySaltAndPepper = ImmutableSet.of(SimpleBBQRegistry.BREAD_SLICE_SKEWER);

        this.getOrCreateTagBuilder(SimpleBBQItemTags.CAN_BE_SEASONED_BY_HONEY).add(allRawSkewers.stream().filter(item -> !cannotBeSeasonedByHoney.contains(item)).toArray(Item[]::new));
        this.getOrCreateTagBuilder(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CHILI_POWDER).add(allRawSkewers.stream().filter(item -> !cannotBeSeasonedByChiliPowder.contains(item)).toArray(Item[]::new));
        this.getOrCreateTagBuilder(SimpleBBQItemTags.CAN_BE_SEASONED_BY_CUMIN).add(allRawSkewers.stream().filter(item -> !cannotBeSeasonedByCumin.contains(item)).toArray(Item[]::new));
        this.getOrCreateTagBuilder(SimpleBBQItemTags.CAN_BE_SEASONED_BY_SALT_AND_PEPPER).add(allRawSkewers.stream().filter(item -> !cannotBeSeasonedBySaltAndPepper.contains(item)).toArray(Item[]::new));

        addConventionItemTags();
    }

    private void addConventionItemTags() {
        this.getOrCreateTagBuilder(ConventionalItemTags.WOODEN_RODS).add(Items.STICK);
    }

    @Override
    public String getName() {
        return "SimpleBBQ Item Tags";
    }
}
