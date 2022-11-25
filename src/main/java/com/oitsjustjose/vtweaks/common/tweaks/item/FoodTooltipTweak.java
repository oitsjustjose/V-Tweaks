package com.oitsjustjose.vtweaks.common.tweaks.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

@Tweak(eventClass = ItemTooltipEvent.class, category = "item")
public class FoodTooltipTweak extends VTweak {
    public enum TooltipSetting {
        NEVER, WITH_SHIFT, ALWAYS
    }

    private ForgeConfigSpec.EnumValue<TooltipSetting> setting;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.setting = builder.comment("Show food hunger & saturation on item hover").defineEnum("foodTooltipSetting", TooltipSetting.WITH_SHIFT);
    }

    @Override
    public void process(Event event) {
        if (this.setting.get() == TooltipSetting.NEVER) return;
        var evt = (ItemTooltipEvent) event;

        var stack = evt.getItemStack();
        var food = stack.getFoodProperties(evt.getEntity());
        var shifting = Screen.hasShiftDown();
        if (!stack.isEdible() || food == null) return;
        if (this.setting.get() == TooltipSetting.WITH_SHIFT && !shifting) return;

        evt.getToolTip().add(getHungerString(food, stack.getRarity()));
        evt.getToolTip().add(getSaturationString((int) (food.getSaturationModifier() * 10), stack.getRarity()));
    }


    private boolean hasBadEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> !x.getFirst().getEffect().isBeneficial());
    }

    private boolean hasGoodEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> x.getFirst().getEffect().isBeneficial());
    }

    private MutableComponent getHungerString(FoodProperties food, Rarity rarity) {
        var ret = new StringBuilder();
        var nutrition = food.getNutrition();
        var anyBadEff = hasBadEffect(food.getEffects());
        var anyGoodEff = hasGoodEffect(food.getEffects());
        var color = anyBadEff ? ChatFormatting.DARK_RED : anyGoodEff ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_GREEN;

        for (int i = 0; i < (nutrition / 2); i++) {
            ret.append(rarity == Rarity.COMMON ? color : rarity.color).append("\u2588");
        }
        if (nutrition % 2 != 0) {
            ret.append(rarity == Rarity.COMMON ? color : rarity.color).append("\u258C");
        }

        var t = new TranslatableContents("vtweaks.hunger.tooltip.text", ret.toString());
        try {
            return t.resolve(null, null, 0);
        } catch (CommandSyntaxException e) {
            return Component.empty();
        }
    }

    private MutableComponent getSaturationString(int saturation, Rarity rarity) {
        var ret = new StringBuilder();
        for (int i = 0; i < saturation / 2; i++) {
            ret.append(rarity == Rarity.COMMON ? ChatFormatting.GREEN : rarity.color).append("\u2588");
        }
        if (saturation % 2 != 0) {
            ret.append(rarity == Rarity.COMMON ? ChatFormatting.GREEN : rarity.color).append("\u258C");
        }
        var t = new TranslatableContents("vtweaks.saturation.tooltip.text", ret.toString());
        try {
            return t.resolve(null, null, 0);
        } catch (CommandSyntaxException e) {
            return Component.empty();
        }
    }
}
