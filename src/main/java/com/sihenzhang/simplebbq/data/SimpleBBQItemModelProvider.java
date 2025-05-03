package com.sihenzhang.simplebbq.data;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.SimpleBBQRegistry;
import com.sihenzhang.simplebbq.util.RLUtils;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.ItemModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.ItemModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

public class SimpleBBQItemModelProvider extends ItemModelProvider {
    public SimpleBBQItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SimpleBBQ.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.blockItem(SimpleBBQRegistry.GRILL_BLOCK);
        this.blockItem(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK);

        this.simpleItem(SimpleBBQRegistry.CHILI_POWDER);
        this.simpleItem(SimpleBBQRegistry.CUMIN);
        this.simpleItem(SimpleBBQRegistry.SALT_AND_PEPPER);

        this.simpleHandheldItem(SimpleBBQRegistry.BEEF_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_BEEF_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.CHICKEN_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_CHICKEN_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.MUTTON_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_MUTTON_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.PORK_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_PORK_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.RABBIT_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_RABBIT_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COD_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_COD_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.SALMON_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.COOKED_SALMON_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.BREAD_SLICE_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.TOAST_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.MUSHROOM_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.ROASTED_MUSHROOM_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.POTATO_SKEWER);
        this.simpleHandheldItem(SimpleBBQRegistry.BAKED_POTATO_SKEWER);
    }

    public ItemModelBuilder blockItem(Block block) {
        return this.blockItem(block, RLUtils.createRL("block/" + getBlockName(block)));
    }

    public ItemModelBuilder blockItem(Block block, ResourceLocation model) {
        return this.withExistingParent(getBlockName(block), model);
    }

    public ItemModelBuilder simpleItem(Item item) {
        return this.basicItem(item);
    }

    public ItemModelBuilder simpleItem(Item item, ResourceLocation texture) {
        return this.withExistingParent(getItemName(item), "item/generated")
                .texture("layer0", texture);
    }

    public ItemModelBuilder item(String name, ResourceLocation texture) {
        return this.withExistingParent(name, "item/generated")
                .texture("layer0", texture);
    }

    public ItemModelBuilder simpleHandheldItem(Item item) {
        return this.simpleHandheldItem(item, RLUtils.createRL("item/" + getItemName(item)));
    }

    public ItemModelBuilder simpleHandheldItem(Item item, ResourceLocation texture) {
        return this.handheldItem(getItemName(item), texture);
    }

    public ItemModelBuilder handheldItem(String name, ResourceLocation texture) {
        return this.withExistingParent(name, "item/handheld")
                .texture("layer0", texture);
    }

    protected static String getBlockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }

    protected static String getItemName(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    @Override
    public String getName() {
        return "SimpleBBQ Item Models";
    }
}
