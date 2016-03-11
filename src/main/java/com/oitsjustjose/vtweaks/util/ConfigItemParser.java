package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ConfigItemParser
{
	public static ArrayList<ItemStack> getParsedItems()
	{
		ArrayList<ItemStack> returnStack = new ArrayList<ItemStack>();

		LogHelper.info(">>Running Config Item Parser");

		for (String s : Config.challengerMobLootTable)
		{
			// Splits the string apart by uncommon characters
			// Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>

			String[] parts = s.split("[\\W]");

			if (parts.length == 4)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
					returnStack.add(new ItemStack(temp.getItem(), Integer.parseInt(parts[3], Integer.parseInt(parts[2]))));
			}
			else if (parts.length == 3)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
				{
					int qty = Integer.parseInt(parts[2]);
					returnStack.add(new ItemStack(temp.getItem(), qty, 0));
				}
			}
			else if (parts.length == 2)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
					returnStack.add(new ItemStack(temp.getItem()));
			}
			// The string was not formatted correctly. User is notified.
			else
			{
				LogHelper.info("There was an error parsing item " + s + " as a challenger mob drop. Please confirm that your formatting is correct.");
			}
		}

		return returnStack;

	}

	public static ItemStack findItemStack(String modid, String name)
	{
		if (GameRegistry.findItem(modid, name) != null)
			return new ItemStack(GameRegistry.findItem(modid, name), 1);
		else if (GameRegistry.findBlock(modid, name) != null)
			if (Item.getItemFromBlock(GameRegistry.findBlock(modid, name)) != null)
				return new ItemStack(Item.getItemFromBlock(GameRegistry.findBlock(modid, name)), 1);

		return null;
	}
}