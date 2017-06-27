package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.VTweaks;

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
		if (!VTweaks.config.enableRecipeHorseArmor || event.getLeft().isEmpty() || event.getRight().isEmpty())
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
		if (VTweaks.config.enableEnchHypermending)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.enchantments.hypermending);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.NETHER_STAR)
			{
				if (event.getRight().getCount() == 1)
				{
					event.setCost(VTweaks.config.hypermendingXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}

		if (VTweaks.config.enableEnchAutosmelt)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.enchantments.autosmelt);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.LAVA_BUCKET)
				if (event.getRight().getCount() == 1)
				{
					event.setCost(VTweaks.config.autosmeltXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
		}

		if (VTweaks.config.enableEnchStepboost)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.enchantments.stepboost);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() instanceof ItemBlock)
			{
				Block inputBlock = Block.getBlockFromItem(event.getRight().getItem());
				if (inputBlock.getRegistryName().toString().contains("stair") && event.getRight().getCount() == 16)
				{
					event.setCost(VTweaks.config.stepboostXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}

		if (VTweaks.config.enableEnchLumbering)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.enchantments.lumbering);

			if (event.getLeft().isEmpty() || event.getRight().isEmpty())
				return;

			boolean damaged = event.getRight().getItemDamage() > 0;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.GOLDEN_AXE)
			{
				if (!damaged)
				{
					event.setCost(VTweaks.config.lumberingXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(ItemStack.EMPTY);
			}
		}
	}
}
