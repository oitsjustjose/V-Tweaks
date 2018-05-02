package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class ToolTips
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void registerTweak(ItemTooltipEvent event)
    {
        if (event.getItemStack().isEmpty())
        {
            return;
        }

        ItemStack stack = event.getItemStack();
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

        // Food tooltip
        if (stack.getItem() instanceof ItemFood)
        {
            // Checks to see if feature is enabled
            if (ModConfig.misc.foodTooltipSetting == 0)
            {
                return;
            }

            ItemFood food = (ItemFood) stack.getItem();
            int hunger = food.getHealAmount(stack);
            float saturation = food.getSaturationModifier(stack) * 10;

            if (ModConfig.misc.foodTooltipSetting == 1)
            {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString(saturation));
            }
            else if (ModConfig.misc.foodTooltipSetting == 2 && shift)
            {
                event.getToolTip().add(getHungerString(hunger));
                event.getToolTip().add(getSaturationString(saturation));
            }
        }

        if (stack.getItem().isDamageable())
        {
            // Checks to see if feature is enabled
            if (ModConfig.misc.durabilityTooltipSetting == 0)
            {
                return;
            }

            if (ModConfig.misc.durabilityTooltipSetting == 1)
            {
                event.getToolTip().add(getDurabilityString(stack));
            }
            else if (ModConfig.misc.durabilityTooltipSetting == 2 && shift)
            {
                event.getToolTip().add(getDurabilityString(stack));
            }
        }
    }

    private String getHungerString(int hunger)
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
        return ret.toString();
    }

    private String getSaturationString(float saturation)
    {
        if (saturation <= 1)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.DARK_RED + "\u2588";
        }
        if (saturation <= 2)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.RED + "\u2588";
        }
        if (saturation <= 3)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.GOLD + "\u2588";
        }
        if (saturation <= 4)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.YELLOW + "\u2588";
        }
        if (saturation <= 5)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.GREEN + "\u2588";
        }
        if (saturation <= 6)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.DARK_GREEN + "\u2588";
        }
        if (saturation <= 7)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.DARK_AQUA + "\u2588";
        }
        if (saturation <= 8)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.BLUE + "\u2588";
        }
        if (saturation <= 9)
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.DARK_PURPLE + "\u2588";
        }
        else
        {
            return TextFormatting.GRAY + "Saturation:" + TextFormatting.LIGHT_PURPLE + "\u2588";
        }
    }

    private String getDurabilityString(ItemStack itemstack)
    {
        String ret = "Durability: ";
        int max = itemstack.getMaxDamage() + 1;
        int damage = itemstack.getItemDamage();
        float percentage = 1 - ((float) damage / (float) max);
        if (percentage >= .9)
        {
            return ret + TextFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .8)
        {
            return ret + TextFormatting.DARK_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .7)
        {
            return ret + TextFormatting.BLUE + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .6)
        {
            return ret + TextFormatting.DARK_AQUA + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .5)
        {
            return ret + TextFormatting.DARK_GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .4)
        {
            return ret + TextFormatting.GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .3)
        {
            return ret + TextFormatting.YELLOW + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .2)
        {
            return ret + TextFormatting.GOLD + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        if (percentage >= .1)
        {
            return ret + TextFormatting.RED + (max - damage) + " / " + max + TextFormatting.RESET;
        }
        return ret + TextFormatting.DARK_RED + (max - damage) + " / " + max + TextFormatting.RESET;
    }
}