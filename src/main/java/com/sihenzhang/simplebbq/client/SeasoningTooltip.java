package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.SimpleBBQ;
import com.sihenzhang.simplebbq.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.function.Supplier;

@EventBusSubscriber(value = Dist.CLIENT, modid = SimpleBBQ.MOD_ID)
public class SeasoningTooltip {
    private static final Supplier<MutableComponent> SPACE = () -> Component.literal("  ");

    @SubscribeEvent
    public static void onTooltip(final ItemTooltipEvent event) {
        var itemStack = event.getItemStack();
        CompoundTag compoundTag = itemStack.get(DataComponents.CUSTOM_DATA).copyTag();
        CompoundTag seasoningTag = compoundTag.getCompound("Seasoning");
        if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
            var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
            var hasEffect = seasoningTag.getBoolean("HasEffect");
            if (!seasoningList.isEmpty()) {
                event.getToolTip().add(I18nUtils.createComponent("tooltip", "seasoned_with").withStyle(ChatFormatting.GRAY));
            }
            for (var i = 0; i < seasoningList.size(); i++) {
                event.getToolTip().add(SPACE.get().append(I18nUtils.createComponent("tooltip", "seasoning." + seasoningList.getString(i)).withStyle(hasEffect ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)));
            }
            if (!seasoningList.isEmpty() && !hasEffect) {
                event.getToolTip().add(I18nUtils.createComponent("tooltip", "seasoning_no_effect").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }
}
