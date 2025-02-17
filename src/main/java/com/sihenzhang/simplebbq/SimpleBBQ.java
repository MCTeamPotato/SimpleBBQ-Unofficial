package com.sihenzhang.simplebbq;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod(SimpleBBQ.MOD_ID)
public class SimpleBBQ {
    public static final String MOD_ID = "simplebbq";

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> new ItemStack(SimpleBBQRegistry.GRILL_BLOCK_ITEM.get()))
            .displayItems((parameters, output) -> {
                // TODO: 还没添加完，还有那个lang文件也没添加⬆️，还有许多地方的.tab()没修改 [SimpleBBQRegistry]
                SimpleBBQRegistry.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            })
            .build()
    );

    public SimpleBBQ() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SimpleBBQConfig.COMMON_CONFIG);

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
                .map(RegistryObject::get)
                .filter(item -> item.isEdible())
                .forEach(event::accept);
        }
    }
}
