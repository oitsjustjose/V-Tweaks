package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ConfigParser
{
	private static Logger LOGGER = LogManager.getLogger(VTweaks.MODID);

	public static void parseItems()
	{
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();

		LOGGER.info(">> Running Config Item Parser");

		for (String s : VTweaks.config.challengerMobLoot)
		{
			// Splits the string apart by uncommon characters
			// Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>

			String[] parts = s.split("[\\W]");

			if (parts.length == 4)
			{
				ItemStack temp = HelperFunctions.findItemStack(parts[0], parts[1]);

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
						LOGGER.info("There was a number formatting issue with entry: " + s + ". It has been skipped.");
					}
				}
			}
			else if (parts.length == 3)
			{
				ItemStack temp = HelperFunctions.findItemStack(parts[0], parts[1]);

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
						LOGGER.info("There was a number formatting issue with entry: " + s + ". It has been skipped.");
					}

				}
			}
			else if (parts.length == 2)
			{
				ItemStack temp = HelperFunctions.findItemStack(parts[0], parts[1]);

				if (temp != null)
				{
					stackList.add(temp.copy());
				}
			}
			// The string was not formatted correctly. User is notified.
			else
			{
				LOGGER.info("There was an error parsing item " + s + " as a challenger mob drop. Please confirm that your formatting is correct.");
			}
		}

		LOGGER.info(">> Config Item Parsing complete!");
		VTweaks.config.setChallengerLootTable(stackList);
	}

	public static void parseBlocks()
	{
		ArrayList<ItemStack> stackList = new ArrayList<ItemStack>();

		LOGGER.info(">> Running Config Block Parser");

		for (String s : VTweaks.config.lavaLossWhitelist)
		{
			// Splits the string apart by uncommon characters
			// Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>
			String[] parts = s.split("[\\W]");

			if (parts.length == 2)
			{
				Block b = Block.REGISTRY.getObject(new ResourceLocation(parts[0], parts[1]));
				if (b != null)
				{
					stackList.add(new ItemStack(b));
					continue;
				}
				else
				{
					LOGGER.info("Could not add entry " + parts[0] + ":" + parts[1]);
				}
			}

			if (parts.length == 3)
			{
				try
				{
					Block b = Block.REGISTRY.getObject(new ResourceLocation(parts[0], parts[1]));
					if (b != null)
					{
						stackList.add(new ItemStack(b, 1, Integer.parseInt(parts[2])));
						continue;
					}
					else
					{
						LOGGER.info("Could not add entry " + parts[0] + ":" + parts[1]);
					}
				}
				catch (NumberFormatException e)
				{
					LOGGER.info("There was an error parsing item " + s + ", please ensure this formatting is correct");
				}
			}

			// The string was not formatted correctly. User is notified.
			else
			{
				LOGGER.info("There was an error parsing item " + s + " as a challenger mob drop. Please confirm that your formatting is correct.");
			}
		}

		LOGGER.info(">> Config Block Parsing complete!");
		VTweaks.config.setLavaLossBlockList(stackList);
	}

}