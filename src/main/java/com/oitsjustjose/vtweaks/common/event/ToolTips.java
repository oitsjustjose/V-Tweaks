package com.oitsjustjose.vtweaks.common.event;

import com.mojang.datafixers.util.Pair;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ToolTips {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerTweak(ItemTooltipEvent event) {
        if (event.getItemStack().isEmpty()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        boolean shift = Screen.hasShiftDown();

        // Food tooltip
        if (stack.getItem().isFood()) {
            // Checks to see if feature is enabled
            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.NEVER) {
                return;
            }

            Food food = stack.getItem().getFood();
            float saturation = food.getSaturation() * 10;

            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.ALWAYS) {
                event.getToolTip().add(getHungerString(food));
                event.getToolTip().add(getSaturationString((int) saturation));
            } else if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.WITH_SHIFT && shift) {
                event.getToolTip().add(getHungerString(food));
                event.getToolTip().add(getSaturationString((int) saturation));
            }
        }

        if (stack.getItem().isDamageable()) {
            // Checks to see if feature is enabled
            if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.NEVER) {
                return;
            }

            if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.ALWAYS) {
                event.getToolTip().add(getDurabilityString(stack));
            } else if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.WITH_SHIFT && shift) {
                event.getToolTip().add(getDurabilityString(stack));
            }
        }
    }

    private boolean hasBadEffect(List<Pair<EffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> !x.getFirst().getPotion().isBeneficial());
    }

    private boolean hasGoodEffect(List<Pair<EffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> x.getFirst().getPotion().isBeneficial());
    }

    private TranslationTextComponent getHungerString(Food food) {
        StringBuilder ret = new StringBuilder();

        int hunger = food.getHealing();

        boolean b = hasBadEffect(food.getEffects());
        boolean g = hasGoodEffect(food.getEffects());

        TextFormatting color = b ? TextFormatting.DARK_RED : g ? TextFormatting.DARK_PURPLE : TextFormatting.DARK_GREEN;

        for (int i = 0; i < (hunger / 2); i++) {
            ret.append(color).append("\u2588");
        }
        if (hunger % 2 != 0) {
            ret.append(color).append("\u258C");
        }

        return new TranslationTextComponent("vtweaks.hunger.tooltip.text", ret.toString());
    }

    private TranslationTextComponent getSaturationString(int saturation) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < saturation / 2; i++) {
            ret.append(TextFormatting.GREEN).append("\u2588");
        }
        if (saturation % 2 != 0) {
            ret.append(TextFormatting.GREEN).append("\u258C");
        }
        return new TranslationTextComponent("vtweaks.saturation.tooltip.text", ret.toString());
    }

    private StringTextComponent getDurabilityString(ItemStack itemstack) {
        String ret = "Durability: ";
        int max = itemstack.getMaxDamage();
        int damage = itemstack.getDamage();
        float percentage = 1 - ((float) damage / (float) max);
        if (percentage >= .9) {
            return new StringTextComponent(
                    ret + TextFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .8) {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .7) {
            return new StringTextComponent(
                    ret + TextFormatting.BLUE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .6) {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_AQUA + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .5) {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_GREEN + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .4) {
            return new StringTextComponent(
                    ret + TextFormatting.GREEN + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .3) {
            return new StringTextComponent(
                    ret + TextFormatting.YELLOW + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .2) {
            return new StringTextComponent(
                    ret + TextFormatting.GOLD + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .1) {
            return new StringTextComponent(
                    ret + TextFormatting.RED + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        return new StringTextComponent(
                ret + TextFormatting.DARK_RED + (max - damage) + " / " + max + TextFormatting.RESET);
    }
}