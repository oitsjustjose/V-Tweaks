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
    private TextFormatting[] gradient = new TextFormatting[]{TextFormatting.DARK_RED, TextFormatting.RED, TextFormatting.GOLD, TextFormatting.YELLOW, TextFormatting.GREEN, TextFormatting.DARK_GREEN, TextFormatting.DARK_AQUA, TextFormatting.BLUE, TextFormatting.DARK_PURPLE, TextFormatting.LIGHT_PURPLE};

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
        StringBuilder ret = new StringBuilder(TextFormatting.GRAY + "Hunger: ");

        for (int i = 0; i < (hunger / 2); i++)
        {
            ret.append(gradient[hunger / 2]).append("\u2588");
        }
        if (hunger % 2 != 0)
        {
            ret.append(gradient[hunger / 2]).append("\u258C");
        }

        return ret.toString();
    }

    private String getSaturationString(float saturation)
    {
        StringBuilder ret = new StringBuilder(TextFormatting.GRAY + "Sat: ");

        for (int i = 0; i < (saturation / 2); i++)
        {
            ret.append(gradient[(int) (saturation / 2)]).append("\u2588");
        }
        if (saturation % 2 != 0)
        {
            ret.append(gradient[(int) (saturation / 2)]).append("\u258C");
        }

        return ret.toString();
    }

    private String getDurabilityString(ItemStack itemstack)
    {
        StringBuilder ret = new StringBuilder("Durability: ");
        int max = itemstack.getMaxDamage() + 1;
        int damage = itemstack.getItemDamage();
        int index = 9 - (int) (((float) damage / (float) max) * 10);
        return ret.append(gradient[index]).append(max - damage).append("/").append(max).append(TextFormatting.RESET).toString();
    }
}