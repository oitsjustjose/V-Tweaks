package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.util.Config;
import org.lwjgl.input.Keyboard;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolTips
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerTweak(ItemTooltipEvent event)
	{
		if (event.getItemStack().isEmpty())
			return;

		ItemStack stack = event.getItemStack();
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

		// Food tooltip
		if (stack.getItem() instanceof ItemFood)
		{
			// Checks to see if feature is enabled
			if (Config.getInstance().foodTooltipSetting == 0)
				return;

			ItemFood food = (ItemFood) stack.getItem();
			int hunger = food.getHealAmount(stack);
			float saturation = food.getSaturationModifier(stack) * 10;

			if (Config.getInstance().foodTooltipSetting == 1)
			{
				event.getToolTip().add(getHungerString(hunger));
				event.getToolTip().add(getSaturationString(saturation));
			}
			else if (Config.getInstance().foodTooltipSetting == 2 && shift)
			{
				event.getToolTip().add(getHungerString(hunger));
				event.getToolTip().add(getSaturationString(saturation));
			}
		}

		if (stack.getItem().isDamageable())
		{
			// Checks to see if feature is enabled
			if (Config.getInstance().durabilityTooltipSetting == 0)
				return;

			if (Config.getInstance().durabilityTooltipSetting == 1)
			{
				event.getToolTip().add(getDurabilityString(stack));
			}
			else if (Config.getInstance().durabilityTooltipSetting == 2 && shift)
			{
				event.getToolTip().add(getDurabilityString(stack));
			}
		}
	}

	private String getHungerString(int hunger)
	{
		String ret = TextFormatting.GRAY + "Hunger:";

		for (int i = 0; i < (hunger / 2); i++)
			ret += TextFormatting.DARK_RED + "\u25A0";
		if (hunger % 2 != 0)
			ret += TextFormatting.RED + "\u25A0";

		return ret;
	}

	private String getSaturationString(float saturation)
	{
		String ret = TextFormatting.GRAY + "Saturation:";

		if (saturation <= 1)
			return ret += TextFormatting.DARK_RED + "\u25A0";
		if (saturation <= 2)
			return ret += TextFormatting.RED + "\u25A0";
		if (saturation <= 3)
			return ret += TextFormatting.GOLD + "\u25A0";
		if (saturation <= 4)
			return ret += TextFormatting.YELLOW + "\u25A0";
		if (saturation <= 5)
			return ret += TextFormatting.GREEN + "\u25A0";
		if (saturation <= 6)
			return ret += TextFormatting.DARK_GREEN + "\u25A0";
		if (saturation <= 7)
			return ret += TextFormatting.DARK_AQUA + "\u25A0";
		if (saturation <= 8)
			return ret += TextFormatting.BLUE + "\u25A0";
		if (saturation <= 9)
			return ret += TextFormatting.DARK_PURPLE + "\u25A0";
		else
			return ret += TextFormatting.LIGHT_PURPLE + "\u25A0";
	}

	private String getDurabilityString(ItemStack itemstack)
	{
		String ret = "Durability: ";
		int max = itemstack.getMaxDamage() + 1;
		int damage = itemstack.getItemDamage();
		float percentage = 1 - ((float) damage / (float) max);
		if (percentage >= .9)
			return ret = ret + TextFormatting.LIGHT_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .8)
			return ret = ret + TextFormatting.DARK_PURPLE + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .7)
			return ret = ret + TextFormatting.BLUE + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .6)
			return ret = ret + TextFormatting.DARK_AQUA + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .5)
			return ret = ret + TextFormatting.DARK_GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .4)
			return ret = ret + TextFormatting.GREEN + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .3)
			return ret = ret + TextFormatting.YELLOW + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .2)
			return ret = ret + TextFormatting.GOLD + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage >= .1)
			return ret = ret + TextFormatting.RED + (max - damage) + " / " + max + TextFormatting.RESET;
		if (percentage < .1)
			return ret = ret + TextFormatting.DARK_RED + (max - damage) + " / " + max + TextFormatting.RESET;
		return ret = ret + (max - damage) + " / " + max;
	}
}