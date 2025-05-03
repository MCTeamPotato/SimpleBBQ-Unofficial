package com.sihenzhang.simplebbq.thirdparty.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface HarvestCheckCallback {
    Event<HarvestCheckCallback> EVENT = EventFactory.createArrayBacked(HarvestCheckCallback.class, (callbacks) -> (player, state, canHarvest) -> {
        for (HarvestCheckCallback callback : callbacks) {
            return callback.onHarvestCheck(player, state, canHarvest);
        }
        return canHarvest;
    });

    boolean onHarvestCheck(Player player, BlockState state, boolean canHarvest);
}
