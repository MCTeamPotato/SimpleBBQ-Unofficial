package com.sihenzhang.simplebbq.mixin;

import com.sihenzhang.simplebbq.thirdparty.extensions.BlockExtension;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin implements BlockExtension {
}
