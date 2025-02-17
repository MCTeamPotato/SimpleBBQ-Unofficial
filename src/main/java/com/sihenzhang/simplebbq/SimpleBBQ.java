package com.sihenzhang.simplebbq;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(SimpleBBQ.MOD_ID)
public class SimpleBBQ {
    public static final String MOD_ID = "simplebbq";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> new ItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM.get()))
            .displayItems((parameters, output) -> {
                // TODO: 还没添加完，还有那个lang文件也没添加⬆️，还有许多地方的.tab()没修改 [SimpleBBQRegistry]
                SimpleBBQRegistry.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            })
            .build()
    );

    public SimpleBBQ(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, SimpleBBQConfig.COMMON_CONFIG);

        CREATIVE_MODE_TABS.register(modEventBus);

        SimpleBBQRegistry.ITEMS.register(modEventBus);
        SimpleBBQRegistry.BLOCKS.register(modEventBus);
        SimpleBBQRegistry.BLOCK_ENTITIES.register(modEventBus);
        SimpleBBQRegistry.PARTICLE_TYPES.register(modEventBus);
        SimpleBBQVillagers.register(modEventBus);
        //SimpleBBQRegistry.POI_TYPES.register(modEventBus);
        //SimpleBBQRegistry.PROFESSIONS.register(modEventBus);
        SimpleBBQRegistry.RECIPE_TYPES.register(modEventBus);
        SimpleBBQRegistry.RECIPE_SERIALIZERS.register(modEventBus);


        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            // 添加食物类物品到食物和饮料标签页
            SimpleBBQRegistry.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(item -> item.getFoodProperties(item.getDefaultInstance(), null) != null)
                .forEach(event::accept);
        }
    }
}
