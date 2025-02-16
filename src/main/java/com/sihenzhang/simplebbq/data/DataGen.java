package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SimpleBBQ.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var helper = event.getExistingFileHelper();
        
        if (event.includeServer()) {
            var blockTagsProvider = new SimpleBBQBlockTagsProvider(packOutput, event.getLookupProvider(), helper);
            generator.addProvider(event.includeServer(), blockTagsProvider);
            generator.addProvider(event.includeServer(), new SimpleBBQItemTagsProvider(packOutput, event.getLookupProvider(), blockTagsProvider.contentsGetter(), helper));
            generator.addProvider(event.includeServer(), new SimpleBBQLootTableProvider(packOutput));
            generator.addProvider(event.includeServer(), new SimpleBBQRecipeProvider(packOutput));
        }
        
        if (event.includeClient()) {
            var blockStateProvider = new SimpleBBQBlockStateProvider(packOutput, helper);
            generator.addProvider(event.includeClient(), blockStateProvider);
            generator.addProvider(event.includeClient(), new SimpleBBQItemModelProvider(packOutput, helper));
        }
    }
}
