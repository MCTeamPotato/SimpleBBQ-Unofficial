package com.sihenzhang.simplebbq.thirdparty.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LivingEntityUseItemEvents {

    public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, (callbacks) -> (entity, item, duration) -> {
        for (Start callback : callbacks) {
            int newDuration = callback.onStart(entity, item, duration);
            if (newDuration != duration) return newDuration;
        }
        return duration;
    });

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, (callbacks) -> (entity, item, duration) -> {
        for (Tick callback : callbacks) {
            int newDuration = callback.onTick(entity, item, duration);
            if (newDuration != duration) return newDuration;
        }
        return duration;
    });

    public static final Event<Stop> STOP = EventFactory.createArrayBacked(Stop.class, (callbacks) -> (entity, item, duration) -> {
        for (Stop callback : callbacks) {
            return callback.onStop(entity, item, duration);
        }
        return false;
    });

    public static final Event<Finish> FINISH = EventFactory.createArrayBacked(Finish.class, (callbacks) -> (entity, item, duration, result) -> {
        for (Finish callback : callbacks) {
            ItemStack itemStack = callback.onFinish(entity, item, duration, result);
            if (itemStack != null) return itemStack;
        }
        return null;
    });

    @FunctionalInterface
    public interface Start {
        int onStart(LivingEntity entity, ItemStack item, int duration);
    }

    @FunctionalInterface
    public interface Tick {
        int onTick(LivingEntity entity, ItemStack item, int duration);
    }

    @FunctionalInterface
    public interface Stop {
        boolean onStop(LivingEntity entity, ItemStack item, int duration);
    }

    @FunctionalInterface
    public interface Finish {
        ItemStack onFinish(LivingEntity entity, ItemStack item, int duration, ItemStack result);
    }
}
