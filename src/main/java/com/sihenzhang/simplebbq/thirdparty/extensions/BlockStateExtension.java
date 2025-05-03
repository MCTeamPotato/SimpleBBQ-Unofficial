package com.sihenzhang.simplebbq.thirdparty.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface BlockStateExtension {
    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param level  The current level
     * @param pos    The block's current position
     * @param player The player damaging the block
     * @return True to spawn the drops
     */
    default boolean canHarvestBlock(BlockGetter level, BlockPos pos, Player player) {
        Block block = ((BlockState) this).getBlock();
        return block.canHarvestBlock((BlockState) this, level, pos, player);
    }

    /**
     * Called when a player removes a block. This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * This function is called on both the logical client and logical server.
     *
     * @param level       The current level
     * @param player      The player damaging the block, may be null
     * @param pos         Block position in level
     * @param willHarvest The result of {@link #canHarvestBlock}, if called on the server by a non-creative player, otherwise always false.
     * @param fluid       The current fluid and block state for the position in the level.
     * @return True if the block is actually destroyed.
     */
    default boolean onDestroyedByPlayer(Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        Block block = ((BlockState) this).getBlock();
        return block.onDestroyedByPlayer((BlockState) this, level, pos, player, willHarvest, fluid);
    }

    /**
     * Determines if this block should set fire and deal fire damage
     * to entities coming into contact with it.
     *
     * @param level The current level
     * @param pos   Block position in level
     * @return True if the block should deal damage
     */
    default boolean isBurning(BlockGetter level, BlockPos pos) {
        Block block = ((BlockState) this).getBlock();
        return block.isBurning((BlockState) this, level, pos);
    }
}
