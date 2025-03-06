package com.oitsjustjose.vtweaks.common.tweaks.item;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.I18n;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;

@Tweak(category = "client.tooltips")
public class DurabilityTooltipTweak extends VTweak {
    /* A mapping of colors for the dura tooltip, where round to 0 leads to Dark Red and round to 9 leads to Light purple */
    private final ArrayList<ChatFormatting> ColorByIndex = Lists.newArrayList(
            ChatFormatting.DARK_RED,
            ChatFormatting.RED,
            ChatFormatting.GOLD,
            ChatFormatting.YELLOW,
            ChatFormatting.GREEN,
            ChatFormatting.DARK_GREEN,
            ChatFormatting.DARK_AQUA,
            ChatFormatting.BLUE,
            ChatFormatting.DARK_PURPLE,
            ChatFormatting.LIGHT_PURPLE
    );
    private ModConfigSpec.EnumValue<TooltipSetting> setting;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.setting = builder.comment("Show tool durability on item hover").defineEnum("durabilityTooltipSetting", TooltipSetting.WITH_SHIFT);
    }

    @SubscribeEvent
    public void process(ItemTooltipEvent evt) {
        if (this.setting.get() == TooltipSetting.NEVER) return;
        var stack = evt.getItemStack();
        var shifting = evt.getFlags().hasShiftDown();
        if (!stack.isDamageableItem()) return;
        if (this.setting.get() == TooltipSetting.WITH_SHIFT && !shifting) return;
        evt.getToolTip().add(getDurabilityString(stack));
    }

    private MutableComponent getDurabilityString(ItemStack itemstack) {
        var maxDamage = itemstack.getMaxDamage();
        var currDamage = itemstack.getDamageValue();
        var percentHealth = 1 - ((float) currDamage / (float) maxDamage);
        var normalized = Math.max(Math.min(Math.round(percentHealth * 10) - 1, 9), 0);

        var comp = I18n.Translate("vtweaks.durability.tooltip.text").copy();
        var comp2 = Component.literal((maxDamage - currDamage) + "/" + maxDamage).withStyle(ColorByIndex.get(normalized));

        return comp.append(comp2);
    }

    public enum TooltipSetting {
        NEVER, WITH_SHIFT, ALWAYS
    }
}
