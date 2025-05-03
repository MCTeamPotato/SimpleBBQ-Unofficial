package com.sihenzhang.simplebbq.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;
import java.util.Set;

@Mixin(PoiTypes.class)
public interface PoiTypesInvoker {
    @Invoker("getBlockStates")
    static Set<BlockState> simplebbq$getBlockStates(Block block) {
        throw new AssertionError();
    }

    @Invoker("registerBlockStates")
    static void simplebbq$registerBlockStates(Holder<PoiType> holder, Set<BlockState> set) {
        throw new AssertionError();
    }
}
