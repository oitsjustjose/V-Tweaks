package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolTips
{
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerTweak(ItemTooltipEvent event)
    {
        if (event.getItemStack().isEmpty())
        {
            return;
        }

        ItemStack stack = event.getItemStack();
        boolean shift = Screen.hasShiftDown();

        // Food tooltip
        if (stack.getItem().isFood())
        {
            // Checks to see if feature is enabled
            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.NEVER)
            {
                return;
            }

            Food food = stack.getItem().getFood();
            int hunger = food.getHealing();
            float saturation = food.getSaturation() * 10;

            if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.ALWAYS)
            {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString((int) saturation));
            }
            else if (CommonConfig.FOOD_TOOLTIP.get() == CommonConfig.FoodTooltips.WITH_SHIFT && shift)
            {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString((int) saturation));
            }
        }

        if (stack.getItem().isDamageable())
        {
            // Checks to see if feature is enabled
            if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.NEVER)
            {
                return;
            }

            if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.ALWAYS)
            {
                event.getToolTip().add(getDurabilityString(stack));
            }
            else if (CommonConfig.DURABILITY_TOOLTIP.get() == CommonConfig.DurabilityTooltips.WITH_SHIFT && shift)
            {
                event.getToolTip().add(getDurabilityString(stack));
            }
        }
    }

    private StringTextComponent getHungerString(int hunger)
    {
        StringBuilder ret = new StringBuilder(TextFormatting.GRAY + "Hunger:");

        for (int i = 0; i < (hunger / 2); i++)
        {
            ret.append(TextFormatting.DARK_RED).append("\u2588");
        }
        if (hunger % 2 != 0)
        {
            ret.append(TextFormatting.DARK_RED).append("\u258C");
        }
        return new StringTextComponent(ret.toString());
    }

    private StringTextComponent getSaturationString(int saturation)
    {
        StringBuilder ret = new StringBuilder(TextFormatting.GRAY + "Sat:");
        for (int i = 0; i < saturation / 2; i++)
        {
            ret.append(TextFormatting.RED).append("\u2588");
        }
        if (saturation % 2 != 0)
        {
            ret.append(TextFormatting.RED).append("\u258C");
        }
        return new StringTextComponent(ret.toString());
    }

    private StringTextComponent getDurabilityString(ItemStack itemstack)
    {
        String ret = "Durability: ";
        int max = itemstack.getMaxDamage() + 1;
        int damage = itemstack.getDamage();
        float percentage = 1 - ((float) damage / (float) max);
        if (percentage >= .9)
        {
            return new StringTextComponent(
                    ret + TextFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .8)
        {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .7)
        {
            return new StringTextComponent(
                    ret + TextFormatting.BLUE + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .6)
        {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_AQUA + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .5)
        {
            return new StringTextComponent(
                    ret + TextFormatting.DARK_GREEN + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .4)
        {
            return new StringTextComponent(
                    ret + TextFormatting.GREEN + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .3)
        {
            return new StringTextComponent(
                    ret + TextFormatting.YELLOW + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .2)
        {
            return new StringTextComponent(
                    ret + TextFormatting.GOLD + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        if (percentage >= .1)
        {
            return new StringTextComponent(
                    ret + TextFormatting.RED + (max - damage) + " / " + max + TextFormatting.RESET);
        }
        return new StringTextComponent(
                ret + TextFormatting.DARK_RED + (max - damage) + " / " + max + TextFormatting.RESET);
    }
}