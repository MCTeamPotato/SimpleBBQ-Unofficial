package com.sihenzhang.simplebbq.client;

import com.sihenzhang.simplebbq.util.I18nUtils;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public class SeasoningTooltip {
    private static final Supplier<MutableComponent> SPACE = () -> Component.literal("  ");

    public static void onTooltip() {
        ItemTooltipCallback.EVENT.register((itemStack, tooltipFlag, list) -> {
            var seasoningTag = itemStack.getTagElement("Seasoning");
            if (seasoningTag != null && seasoningTag.contains("SeasoningList", Tag.TAG_LIST)) {
                var seasoningList = seasoningTag.getList("SeasoningList", Tag.TAG_STRING);
                var hasEffect = seasoningTag.getBoolean("HasEffect");
                if (!seasoningList.isEmpty()) {
                    list.add(I18nUtils.createComponent("tooltip", "seasoned_with").withStyle(ChatFormatting.GRAY));
                }
                for (var i = 0; i < seasoningList.size(); i++) {
                    list.add(SPACE.get().append(I18nUtils.createComponent("tooltip", "seasoning." + seasoningList.getString(i)).withStyle(hasEffect ? ChatFormatting.YELLOW : ChatFormatting.DARK_GRAY)));
                }
                if (!seasoningList.isEmpty() && !hasEffect) {
                    list.add(I18nUtils.createComponent("tooltip", "seasoning_no_effect").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
        });
    }
}
