package com.sihenzhang.simplebbq.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.sihenzhang.simplebbq.thirdparty.event.LivingEntityUseItemEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected int useItemRemaining;

    @Shadow public abstract int getUseItemRemainingTicks();

    @Shadow public abstract ItemStack getUseItem();

    @WrapOperation(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int simplebbq$onStart(ItemStack itemStack, LivingEntity entity, Operation<Integer> original) {
        return LivingEntityUseItemEvents.START.invoker().onStart((LivingEntity) (Object) this, itemStack, itemStack.getUseDuration(entity));
    }

    @Inject(method = "updateUsingItem", at = @At("HEAD"))
    private void simplebbq$onTick(ItemStack usingItem, CallbackInfo ci) {
        if (!usingItem.isEmpty()) {
            var entity = (LivingEntity) (Object) this;
            this.useItemRemaining = LivingEntityUseItemEvents.TICK.invoker().onTick((LivingEntity) (Object) this, usingItem, usingItem.getUseDuration(entity));
        }
    }

    @WrapOperation(method = "releaseUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;releaseUsing(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V"))
    private void simplebbq$onStop(ItemStack instance, Level level, LivingEntity livingEntity, int timeLeft, Operation<Void> original) {
        if (LivingEntityUseItemEvents.STOP.invoker().onStop((LivingEntity) (Object) this, this.getUseItem(), getUseItemRemainingTicks())) {
            this.getUseItem().releaseUsing(level, (LivingEntity) (Object) this, this.getUseItemRemainingTicks());
        }
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
