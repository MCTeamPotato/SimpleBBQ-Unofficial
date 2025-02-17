package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SimpleBBQ.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var helper = event.getExistingFileHelper();
        var provider = event.getLookupProvider();

        if (event.includeServer()) {
            var blockTagsProvider = new SimpleBBQBlockTagsProvider(packOutput, provider, helper);
            generator.addProvider(event.includeServer(), blockTagsProvider);
            generator.addProvider(event.includeServer(), new SimpleBBQItemTagsProvider(packOutput, provider, blockTagsProvider.contentsGetter(), helper));
            generator.addProvider(event.includeServer(), new SimpleBBQLootTableProvider(packOutput,provider));
            generator.addProvider(event.includeServer(), new SimpleBBQRecipeProvider(packOutput,provider));
        }
        
        if (event.includeClient()) {
            var blockStateProvider = new SimpleBBQBlockStateProvider(packOutput, helper);
            generator.addProvider(event.includeClient(), blockStateProvider);
            generator.addProvider(event.includeClient(), new SimpleBBQItemModelProvider(packOutput, helper));
        }
    }
}
