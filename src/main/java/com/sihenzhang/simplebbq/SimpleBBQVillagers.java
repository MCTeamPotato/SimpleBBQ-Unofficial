package com.sihenzhang.simplebbq;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class SimpleBBQVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SimpleBBQ.MOD_ID);

    //public static final RegistryObject<PoiType> SKEWERMAN_POI = POI_TYPES.register("skewerman", () -> new PoiType("skewerman", PoiTypes.getBlockStates(SKEWERING_TABLE_BLOCK.get()), 1, 1));
    //public static final RegistryObject<VillagerProfession> SKEWERMAN = PROFESSIONS.register("skewerman", () -> new VillagerProfession("skewerman", SKEWERMAN_POI.get(), ImmutableSet.of(), ImmutableSet.of(), null));

    public static final RegistryObject<PoiType> SKEWERMAN_POI = POI_TYPES.register("skewerman_poi",
            () -> new PoiType(ImmutableSet.copyOf(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> SKEWERMAN = VILLAGER_PROFESSIONS.register("skewerman",
            () -> new VillagerProfession("skewerman", x -> x.get() == SKEWERMAN_POI.get(),
                    x -> x.get() == SKEWERMAN_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_BUTCHER));


    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, SKEWERMAN_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}