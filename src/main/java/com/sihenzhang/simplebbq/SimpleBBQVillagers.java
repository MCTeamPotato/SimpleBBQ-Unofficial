package com.sihenzhang.simplebbq;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.simplebbq.mixin.PoiTypesInvoker;
import com.sihenzhang.simplebbq.util.RLUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class SimpleBBQVillagers {
    public static final PoiType SKEWERMAN_POI = registerPoiType(
            "skewerman_poi",
            PoiTypesInvoker.simplebbq$getBlockStates(SimpleBBQRegistry.SKEWERING_TABLE_BLOCK)
    );

    public static final VillagerProfession SKEWERMAN = registerVillagerProfession("skewerman",
            new VillagerProfession("skewerman", x -> x.value() == SKEWERMAN_POI,
                    x -> x.value() == SKEWERMAN_POI, ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_BUTCHER));


    public static PoiType registerPoiType(String id, Set<BlockState> matchingStates) {
        ResourceKey<PoiType> resourceKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, RLUtils.createRL(id));
        var poiTypeRegistry = Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, resourceKey, new PoiType(matchingStates, 1, 1));
        PoiTypesInvoker.simplebbq$registerBlockStates(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getHolderOrThrow(resourceKey), matchingStates);
        return poiTypeRegistry;
    }

    public static <T extends VillagerProfession> T registerVillagerProfession(String id, T villagerProfession) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, RLUtils.createRL(id), villagerProfession);
    }

    public static void initialize() {
        //NO-OP
    }
}