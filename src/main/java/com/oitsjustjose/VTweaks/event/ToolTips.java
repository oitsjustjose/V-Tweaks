package com.oitsjustjose.vtweaks.event;

import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.util.EnumChatFormatting;
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
		if (event.itemStack == null)
			return;

		ItemStack stack = event.itemStack;
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

		if (stack.getItem() instanceof ItemFood)
		{
			ItemFood food = (ItemFood) stack.getItem();
			int hunger = food.getHealAmount(stack);
			float saturation = food.getSaturationModifier(stack) * 10;

			if (Config.foodToolTips == 0)
				return;
			else if (Config.foodToolTips == 1)
			{
				event.toolTip.add(getHungerString(hunger));
				event.toolTip.add(getSaturationString(saturation));
			}
			else if (Config.foodToolTips == 2)
			{
				if (shift)
				{
					event.toolTip.add(getHungerString(hunger));
					event.toolTip.add(getSaturationString(saturation));
				}
			}
		}

		if (stack.getItem() instanceof ItemWritableBook)
		{
			ItemWritableBook book = (ItemWritableBook) stack.getItem();
			ItemStack bookStack = new ItemStack(book);
			if (bookStack.getTagCompound() == null)
				return;
			Set<String> s = bookStack.getTagCompound().getKeySet();
			for (String x : s)
				event.toolTip.add(x);
		}
	}

	public static String getHungerString(int hunger)
	{
		String ret = EnumChatFormatting.GRAY + "Hunger:";

		for (int i = 0; i < (hunger / 2); i++)
			ret += EnumChatFormatting.DARK_RED + "\u25A0";
		if (hunger % 2 != 0)
			ret += EnumChatFormatting.RED + "\u25A0";

		return ret;
	}

	public static String getSaturationString(float saturation)
	{
		String ret = EnumChatFormatting.GRAY + "Saturation:";

		if (saturation <= 1)
			return ret += EnumChatFormatting.DARK_RED + "\u25A0";
		if (saturation <= 2)
			return ret += EnumChatFormatting.RED + "\u25A0";
		if (saturation <= 3)
			return ret += EnumChatFormatting.GOLD + "\u25A0";
		if (saturation <= 4)
			return ret += EnumChatFormatting.YELLOW + "\u25A0";
		if (saturation <= 5)
			return ret += EnumChatFormatting.DARK_GREEN + "\u25A0";
		if (saturation <= 6)
			return ret += EnumChatFormatting.GREEN + "\u25A0";
		if (saturation <= 7)
			return ret += EnumChatFormatting.BLUE + "\u25A0";
		if (saturation <= 8)
			return ret += EnumChatFormatting.DARK_AQUA + "\u25A0";
		if (saturation <= 9)
			return ret += EnumChatFormatting.DARK_PURPLE + "\u25A0";
		else
			return ret += EnumChatFormatting.LIGHT_PURPLE + "\u25A0";
	}
}