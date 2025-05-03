package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.thirdparty.datagen.DataGenerators;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        ExistingFileHelper existingFileHelper = ExistingFileHelper.withResourcesFromArg();
        DataGenerators generator = new DataGenerators(pack, existingFileHelper);

        var blockTagsProvider = generator.addProvider(SimpleBBQBlockTagsProvider::new);
        generator.addProvider((output, registries, helper) -> {
            return new SimpleBBQItemTagsProvider(output, registries, blockTagsProvider);
        });
        generator.addProvider(SimpleBBQLootTableProvider::new);
        generator.addProvider(SimpleBBQRecipeProvider::new);



        generator.addProvider(SimpleBBQBlockStateProvider::new);
        generator.addProvider(SimpleBBQItemModelProvider::new);
    }
}
