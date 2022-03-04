package com.oitsjustjose.vtweaks.common.event;

import com.mojang.datafixers.util.Pair;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
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
        if (stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null) {
            // Checks to see if feature is enabled
            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.NEVER) {
                return;
            }

            FoodProperties food = stack.getItem().getFoodProperties();
            float saturation = food.getSaturationModifier() * 10;

            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.ALWAYS) {
                event.getToolTip().add(getHungerString(food));
                event.getToolTip().add(getSaturationString((int) saturation));
            } else if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.WITH_SHIFT && shift) {
                event.getToolTip().add(getHungerString(food));
                event.getToolTip().add(getSaturationString((int) saturation));
            }
        }

        if (stack.isDamageableItem()) {
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

    private boolean hasBadEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> !x.getFirst().getEffect().isBeneficial());
    }

    private boolean hasGoodEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> x.getFirst().getEffect().isBeneficial());
    }

    private TranslatableComponent getHungerString(FoodProperties food) {
        StringBuilder ret = new StringBuilder();

        int nutrition = food.getNutrition();

        boolean b = hasBadEffect(food.getEffects());
        boolean g = hasGoodEffect(food.getEffects());

        ChatFormatting color = b ? ChatFormatting.DARK_RED : g ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_GREEN;

        for (int i = 0; i < (nutrition / 2); i++) {
            ret.append(color).append("\u2588");
        }
        if (nutrition % 2 != 0) {
            ret.append(color).append("\u258C");
        }

        return new TranslatableComponent("vtweaks.hunger.tooltip.text", ret.toString());
    }

    private TranslatableComponent getSaturationString(int saturation) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < saturation / 2; i++) {
            ret.append(ChatFormatting.GREEN).append("\u2588");
        }
        if (saturation % 2 != 0) {
            ret.append(ChatFormatting.GREEN).append("\u258C");
        }
        return new TranslatableComponent("vtweaks.saturation.tooltip.text", ret.toString());
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