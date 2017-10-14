package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.item.ItemStack;

public class ConfigParser
{
	private static Logger LOGGER = LogManager.getLogger(VTweaks.MODID);

	public static void parseItems()
	{
		LOGGER.info(">> Running Config Item Parser");
		Config.getInstance().setChallengerLootTable(parse(Config.getInstance().challengerMobLoot, true));
		LOGGER.info(">> Config Item Parsing complete!");
	}

	public static void parseBlocks()
	{
		LOGGER.info(">> Running Config Block Parser");
		Config.getInstance().setLavaLossBlockList(parse(Config.getInstance().lavaLossWhitelist, false));
		LOGGER.info(">> Config Block Parsing complete!");
	}

	/**
	 * @param input
	 *            A string array gathered from a config file
	 * @param includesQty
	 *            true if strings in the array include quantities of the itemstack
	 * @return an ArrayList containing parsed ItemStacks of these
	 */
	private static ArrayList<ItemStack> parse(String[] input, boolean includesQty)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		// Goes through every string in the inputs list
		for (String toSplit : input)
		{
			String[] parts = toSplit.split("[\\W]");
			ItemStack tempStack = HelperFunctions.findItemStack(parts[0], parts[1]);
			if (!tempStack.isEmpty())
			{
				try
				{
					if (parts.length == 2)
					{
						ret.add(tempStack.copy());
						continue;
					}
					else if (parts.length == 3)
					{
						tempStack = new ItemStack(tempStack.getItem(), 1, Integer.parseInt(parts[2]));
						ret.add(tempStack.copy());
					}
					else if (parts.length == 4 && includesQty)
					{
						tempStack = new ItemStack(tempStack.getItem(), Integer.parseInt(parts[3]), Integer.parseInt(parts[2]));
						ret.add(tempStack.copy());
					}
					else
					{
						LOGGER.info("Error parsing " + toSplit + ", please ensure proper formatting!");
						continue;
					}
				}
				catch (NumberFormatException e)
				{
					LOGGER.info("Error parsing " + toSplit + ", please ensure proper formatting!");
					continue;
				}
			}
		}
		return ret;
	}

}
