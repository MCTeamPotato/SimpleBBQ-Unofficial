package com.sihenzhang.simplebbq.thirdparty.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockHelper {
    public static BlockState getRawBlockState(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.hasBlockEntity()/* && level.getBlockEntity(pos) instanceof BlockEntity be*/) {
            return level.getBlockEntity(pos).getBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }
}
