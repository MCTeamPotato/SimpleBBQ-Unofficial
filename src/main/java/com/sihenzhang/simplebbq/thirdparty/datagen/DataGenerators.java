package com.sihenzhang.simplebbq.thirdparty.datagen;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public record DataGenerators(FabricDataGenerator.Pack pack, ExistingFileHelper helper) {
    public <T extends DataProvider> T addProvider(FabricDataGenerator.Pack.Factory<T> factory) {
        return pack.addProvider(factory);
    }

    public <T extends DataProvider> T addProvider(FabricDataGenerator.Pack.RegistryDependentFactory<T> factory) {
        return pack.addProvider(factory);
    }

    public <T extends DataProvider> T addProvider(ExistingFileDependentFactory<T> factory) {
        return pack.addProvider((FabricDataOutput output) -> factory.create(output, helper));
    }

    public <T extends DataProvider> T addProvider(ExistingFileRegistryDependentFactory<T> factory) {
        return pack.addProvider((output, registries) -> factory.create(output, registries, helper));
    }

    @FunctionalInterface
    public interface ExistingFileDependentFactory<T extends DataProvider> {
        T create(FabricDataOutput output, ExistingFileHelper helper);
    }

    @FunctionalInterface
    public interface ExistingFileRegistryDependentFactory<T extends DataProvider> {
        T create(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper);
    }
}
