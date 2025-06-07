package com.sihenzhang.simplebbq.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.simplebbq.thirdparty.event.LivingEntityUseItemEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract int getUseItemRemainingTicks();

    @Shadow public abstract ItemStack getUseItem();

    @WrapOperation(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration()I"))
    private int simplebbq$onStart(ItemStack itemStack, Operation<Integer> original) {
        return LivingEntityUseItemEvents.START.invoker().onStart((LivingEntity) (Object) this, itemStack, itemStack.getUseDuration());
    }

    @Inject(
            method = "completeUsingItem",
            at = @At(
                    value = "INVOKE",
                    shift = At.Shift.BY,
                    by = 2,
                    target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private void simplebbq$onFinish(CallbackInfo ci, @Local ItemStack result) {
        LivingEntityUseItemEvents.FINISH.invoker().onFinish((LivingEntity) (Object) this, this.getUseItem().copy(), this.getUseItemRemainingTicks(), result);
    }
}
