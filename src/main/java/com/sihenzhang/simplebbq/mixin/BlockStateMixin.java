package com.sihenzhang.simplebbq.mixin;

import com.sihenzhang.simplebbq.thirdparty.extensions.BlockStateExtension;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockState.class)
public class BlockStateMixin implements BlockStateExtension {
}
