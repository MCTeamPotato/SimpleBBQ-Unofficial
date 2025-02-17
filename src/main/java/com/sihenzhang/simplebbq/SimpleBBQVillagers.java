package com.sihenzhang.simplebbq;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.InvocationTargetException;

public class SimpleBBQVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, SimpleBBQ.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, SimpleBBQ.MOD_ID);

    //public static final RegistryObject<PoiType> SKEWERMAN_POI = POI_TYPES.register("skewerman", () -> new PoiType("skewerman", PoiTypes.getBlockStates(SKEWERING_TABLE_BLOCK.get()), 1, 1));
    //public static final RegistryObject<VillagerProfession> SKEWERMAN = PROFESSIONS.register("skewerman", () -> new VillagerProfession("skewerman", SKEWERMAN_POI.get(), ImmutableSet.of(), ImmutableSet.of(), null));

    public static final DeferredHolder<PoiType, PoiType> SKEWERMAN_POI = POI_TYPES.register("skewerman_poi",
            () -> new PoiType(ImmutableSet.copyOf(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> SKEWERMAN = VILLAGER_PROFESSIONS.register("skewerman",
            () -> new VillagerProfession("skewerman", x -> x.value() == SKEWERMAN_POI.get(),
                    x -> x.value() == SKEWERMAN_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
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