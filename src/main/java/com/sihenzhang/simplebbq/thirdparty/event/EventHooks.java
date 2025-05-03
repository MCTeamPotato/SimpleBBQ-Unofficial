package com.sihenzhang.simplebbq.thirdparty.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class EventHooks {
    public static boolean doPlayerHarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos) {
        // Call deprecated hasCorrectToolForDrops overload for a fallback value, in turn the non-deprecated overload calls this method
        boolean vanillaValue = player.hasCorrectToolForDrops(state);
        return HarvestCheckCallback.EVENT.invoker().onHarvestCheck(player, state, level, pos, vanillaValue);
    }
}
