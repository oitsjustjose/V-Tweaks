package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
        if (stack.getItem().getFoodProperties() != null) {
            // Checks to see if feature is enabled
            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.NEVER) {
                return;
            }

            FoodProperties food = stack.getItem().getFoodProperties();
            int hunger = food.getNutrition();
            float saturation = food.getSaturationModifier() * 10;

            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.ALWAYS) {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString((int) saturation));
            } else if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.WITH_SHIFT && shift) {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString((int) saturation));
            }
        }

        if (stack.getItem().isDamageable(stack)) {
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

    private TextComponent getHungerString(int hunger) {
        StringBuilder ret = new StringBuilder(ChatFormatting.GRAY + "Hunger:");

        for (int i = 0; i < (hunger / 2); i++) {
            ret.append(ChatFormatting.RED).append("\u2588");
        }
        if (hunger % 2 != 0) {
            ret.append(ChatFormatting.DARK_RED).append("\u258C");
        }
        return new TextComponent(ret.toString());
    }

    private TextComponent getSaturationString(int saturation) {
        StringBuilder ret = new StringBuilder(ChatFormatting.GRAY + "Sat:");
        for (int i = 0; i < saturation / 2; i++) {
            ret.append(ChatFormatting.RED).append("\u2588");
        }
        if (saturation % 2 != 0) {
            ret.append(ChatFormatting.RED).append("\u258C");
        }
        return new TextComponent(ret.toString());
    }

    private TextComponent getDurabilityString(ItemStack itemstack) {
        String ret = "Durability: ";
        int max = itemstack.getMaxDamage();
        int damage = itemstack.getDamageValue();
        float percentage = 1 - ((float) damage / (float) max);
        if (percentage >= .9) {
            return new TextComponent(
                    ret + ChatFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .8) {
            return new TextComponent(
                    ret + ChatFormatting.DARK_PURPLE + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .7) {
            return new TextComponent(
                    ret + ChatFormatting.BLUE + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .6) {
            return new TextComponent(
                    ret + ChatFormatting.DARK_AQUA + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .5) {
            return new TextComponent(
                    ret + ChatFormatting.DARK_GREEN + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .4) {
            return new TextComponent(
                    ret + ChatFormatting.GREEN + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .3) {
            return new TextComponent(
                    ret + ChatFormatting.YELLOW + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .2) {
            return new TextComponent(
                    ret + ChatFormatting.GOLD + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        if (percentage >= .1) {
            return new TextComponent(
                    ret + ChatFormatting.RED + (max - damage) + " / " + max + ChatFormatting.RESET);
        }
        return new TextComponent(
                ret + ChatFormatting.DARK_RED + (max - damage) + " / " + max + ChatFormatting.RESET);
    }
}