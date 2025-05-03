package com.sihenzhang.simplebbq.thirdparty.extensions;

import com.sihenzhang.simplebbq.thirdparty.event.HarvestCheckCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface BlockExtension {
    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param level The current level
     * @param pos The block's current position
     * @param player The player damaging the block
     * @return True to spawn the drops
     */
    default boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (!state.requiresCorrectToolForDrops())
            return HarvestCheckCallback.EVENT.invoker().onHarvestCheck(player, state, true);

        return player.hasCorrectToolForDrops(state);
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param state The current state.
     * @param level The current level
     * @param player The player damaging the block, may be null
     * @param pos Block position in level
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @param fluid The current fluid state at current position
     * @return True if the block is actually destroyed.
     */
    default boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        ((Block) this).playerWillDestroy(level, pos, state, player);
        return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide ? 11 : 3);
    }

    /**
     * Determines if this block should set fire and deal fire damage
     * to entities coming into contact with it.
     *
     * @param level The current level
     * @param pos Block position in level
     * @return True if the block should deal damage
     */
    default boolean isBurning(BlockState state, BlockGetter level, BlockPos pos) {
        return this == Blocks.FIRE || this == Blocks.LAVA;
    }
}
