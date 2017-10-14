package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.VTweaks;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Recipes
{
	@SubscribeEvent
	public void registerHorseArmorRecipes(AnvilUpdateEvent event)
	{
		if (!Config.getInstance().enableRecipeHorseArmor || event.getLeft().isEmpty() || event.getRight().isEmpty())
			return;

		Item left = event.getLeft().getItem();
		Item right = event.getRight().getItem();

		boolean damaged = event.getLeft().getItemDamage() > 0 || event.getRight().getItemDamage() > 0;

		if ((left == Items.DIAMOND_LEGGINGS && right == Items.DIAMOND_LEGGINGS) && !damaged)
		{
			event.setCost(12);
			event.setOutput(new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		}
		if ((left == Items.GOLDEN_LEGGINGS && right == Items.GOLDEN_LEGGINGS) && !damaged)
		{
			event.setCost(8);
			event.setOutput(new ItemStack(Items.GOLDEN_HORSE_ARMOR));
		}
		if ((left == Items.IRON_LEGGINGS && right == Items.IRON_LEGGINGS) && !damaged)
		{
			event.setCost(4);
			event.setOutput(new ItemStack(Items.IRON_HORSE_ARMOR));
		}
	}

	@SubscribeEvent
	public void registerBookRecipes(AnvilUpdateEvent event)
	{
		if (Config.getInstance().enableEnchHypermending)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.getInstance().hypermending);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.NETHER_STAR)
			{
				if (event.getRight().getCount() == 1)
				{
					event.setCost(Config.getInstance().hypermendingXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}

		if (Config.getInstance().enableEnchAutosmelt)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.getInstance().autosmelt);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.LAVA_BUCKET)
				if (event.getRight().getCount() == 1)
				{
					event.setCost(Config.getInstance().autosmeltXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
		}

		if (Config.getInstance().enableEnchStepboost)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.getInstance().stepboost);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() instanceof ItemBlock)
			{
				Block inputBlock = Block.getBlockFromItem(event.getRight().getItem());
				if (inputBlock.getRegistryName().toString().contains("stair") && event.getRight().getCount() == 16)
				{
					event.setCost(Config.getInstance().stepboostXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}

		if (Config.getInstance().enableEnchLumbering)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.getInstance().lumbering);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			boolean damaged = event.getRight().getItemDamage() > 0;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.GOLDEN_AXE)
			{
				if (!damaged)
				{
					event.setCost(Config.getInstance().lumberingXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}
	}
}
