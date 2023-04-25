package com.oitsjustjose.vtweaks.common.tweaks.item;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

@Tweak(eventClass = ItemTooltipEvent.class, category = "item")
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
    private ForgeConfigSpec.EnumValue<TooltipSetting> setting;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.setting = builder.comment("Show tool durability on item hover").defineEnum("durabilityTooltipSetting", TooltipSetting.WITH_SHIFT);
    }

    @SubscribeEvent
    public void process(ItemTooltipEvent evt) {
        if (this.setting.get() == TooltipSetting.NEVER) return;
        var stack = evt.getItemStack();
        var shifting = Screen.hasShiftDown();
        if (!stack.isDamageableItem()) return;
        if (this.setting.get() == TooltipSetting.WITH_SHIFT && !shifting) return;
        evt.getToolTip().add(getDurabilityString(stack));
    }

    private MutableComponent getDurabilityString(ItemStack itemstack) {
        var ret = "Durability: ";
        var c = Component.empty();

        var max = itemstack.getMaxDamage();
        var damage = itemstack.getDamageValue();
        var percentage = 1 - ((float) damage / (float) max);
        var scaled = Math.max(Math.min(Math.round(percentage * 10) - 1, 9), 0);
        return c.append(ret + ColorByIndex.get(scaled) + (max - damage) + "/" + max + ChatFormatting.RESET);
    }

    public enum TooltipSetting {
        NEVER, WITH_SHIFT, ALWAYS
    }
}
