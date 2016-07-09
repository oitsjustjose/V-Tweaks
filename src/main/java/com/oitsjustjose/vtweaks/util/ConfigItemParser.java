package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ConfigItemParser
{
	public static void parseItems()
	{
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();

		LogHelper.info(">> Running Config Item Parser");

		for (String s : VTweaks.modConfig.challengerMobLootTable)
		{
			// Splits the string apart by uncommon characters
			// Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>

			String[] parts = s.split("[\\W]");

			if (parts.length == 4)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
				{
					try
					{
						int meta = Integer.parseInt(parts[2]);
						int qty = Integer.parseInt(parts[3]);
						temp.setItemDamage(meta);
						temp.stackSize = qty;
						stackList.add(temp.copy());
					}
					catch (NumberFormatException e)
					{
						LogHelper.info("There was a number formatting issue with entry: " + s + ". It has been skipped.");
					}
				}
			}
			else if (parts.length == 3)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
				{
					try
					{
						int qty = Integer.parseInt(parts[2]);
						temp.stackSize = qty;
						stackList.add(temp.copy());
					}
					catch (NumberFormatException e)
					{
						LogHelper.info("There was a number formatting issue with entry: " + s + ". It has been skipped.");
					}
					
				}
			}
			else if (parts.length == 2)
			{
				ItemStack temp = findItemStack(parts[0], parts[1]);

				if (temp != null)
				{
					stackList.add(new ItemStack(temp.copy().getItem()));
				}
			}
			// The string was not formatted correctly. User is notified.
			else
			{
				LogHelper.info("There was an error parsing item " + s + " as a challenger mob drop. Please confirm that your formatting is correct.");
			}
		}

		LogHelper.info(">> Config Item Parsing complete!");
		VTweaks.modConfig.setChallengerLootTable(stackList);
	}

	public static ItemStack findItemStack(String modid, String name)
	{
		ResourceLocation resLoc = new ResourceLocation(modid, name);
		if (Item.REGISTRY.containsKey(resLoc))
			return new ItemStack(Item.REGISTRY.getObject(resLoc));
		else if (Block.REGISTRY.containsKey(resLoc))
			if (Item.getItemFromBlock(Block.REGISTRY.getObject(resLoc)) != null)
				return new ItemStack(Item.getItemFromBlock(Block.REGISTRY.getObject(resLoc)), 1);

		return null;
	}
}