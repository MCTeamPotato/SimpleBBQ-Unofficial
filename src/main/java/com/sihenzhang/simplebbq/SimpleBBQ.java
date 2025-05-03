package com.sihenzhang.simplebbq;

import com.sihenzhang.simplebbq.levelgen.VillageStructures;
import com.sihenzhang.simplebbq.util.RLUtils;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.config.ModConfig;

public class SimpleBBQ implements ModInitializer {
    public static final String MOD_ID = "simplebbq";

    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, RLUtils.createRL("tab"));

    public static final CreativeModeTab TAB = FabricItemGroup.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> new ItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM))
            .displayItems((parameters, output) -> {
                // TODO: 还没添加完，还有那个lang文件也没添加⬆️，还有许多地方的.tab()没修改 [SimpleBBQRegistry]
                SimpleBBQRegistry.ITEMS.values().forEach(item -> output.accept(item));
            })
            .build();

    @Override
    public void onInitialize() {
        NeoForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, SimpleBBQConfig.COMMON_CONFIG);

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, TAB);

        SimpleBBQRegistry.initialize();
        SimpleBBQVillagers.initialize();
        SimpleBBQEvents.initialize();
        VillageStructures.addNewVillageBuilding();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            // 添加食物类物品到食物和饮料标签页
            SimpleBBQRegistry.ITEMS.forEach((id, item) -> {
                if (item.components().has(DataComponents.FOOD)) {
                    entries.accept(item);
                }
            });
        });
    }
}
