package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes
{
	public static void registerRecipes()
	{
		if (VTweaks.config.disenchant && !(Loader.isModLoaded("Botania") || Loader.isModLoaded("ThaumicTinkerer")))
		{
			CraftingManager.getInstance().getRecipeList().add(new DisenchantRecipes(Items.PAPER));
			RecipeSorter.register("VTweaks:disenchanting", DisenchantRecipes.class, Category.SHAPELESS, "");
		}
	}

	@SubscribeEvent
	public void registerEvent(AnvilUpdateEvent event)
	{
		registerBookRecipes(event);

		if (VTweaks.config.horseArmor)
			registerHorseArmorRecipes(event);
	}

	public void registerHorseArmorRecipes(AnvilUpdateEvent event)
	{
		if (event.getLeft() == null || event.getRight() == null)
			return;

		Item left = event.getLeft().getItem();
		Item right = event.getRight().getItem();

		boolean damaged = event.getLeft().getItemDamage() > 0 || event.getRight().getItemDamage() > 0;

		if ((left == Items.DIAMOND_LEGGINGS && right == Items.DIAMOND_LEGGINGS) && !damaged)
		{
			event.setCost(35);
			event.setOutput(new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		}
		if ((left == Items.GOLDEN_LEGGINGS && right == Items.GOLDEN_LEGGINGS) && !damaged)
		{
			event.setCost(30);
			event.setOutput(new ItemStack(Items.GOLDEN_HORSE_ARMOR));
		}
		if ((left == Items.IRON_LEGGINGS && right == Items.IRON_LEGGINGS) && !damaged)
		{
			event.setCost(20);
			event.setOutput(new ItemStack(Items.IRON_HORSE_ARMOR));
		}
	}

	public void registerBookRecipes(AnvilUpdateEvent event)
	{
		if (VTweaks.config.hypermendingID > 0)
		{
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
			Items.ENCHANTED_BOOK.addEnchantment(book, new EnchantmentData(Enchantments.hyperMending, 1));

			if (event.getLeft() == null || event.getRight() == null)
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.NETHER_STAR)
			{
				if (event.getRight().stackSize == 1)
				{
					event.setCost(VTweaks.config.hypermendingXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(null);
			}
		}

		if (VTweaks.config.autosmeltID > 0)
		{
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
			Items.ENCHANTED_BOOK.addEnchantment(book, new EnchantmentData(Enchantments.autosmelt, 1));

			if (event.getLeft() == null || event.getRight() == null)
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.LAVA_BUCKET)
				if (event.getRight().stackSize == 1)
				{
					event.setCost(VTweaks.config.autosmeltXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(null);
		}
		
		if(VTweaks.config.stepboostID > 0)
		{
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
			Items.ENCHANTED_BOOK.addEnchantment(book, new EnchantmentData(Enchantments.stepboost, 1));

			if (event.getLeft() == null || event.getRight() == null)
				return;

			if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() instanceof ItemBlock)
			{
				Block inputBlock = Block.getBlockFromItem(event.getRight().getItem());
				if (inputBlock.getRegistryName().toString().contains("stair") && event.getRight().stackSize == 16)
				{
					event.setCost(VTweaks.config.stepboostXPCost);
					event.setOutput(book);
				}
				else
					event.setOutput(null);
			}
		}

		if (VTweaks.config.lumberingID > 0)
		{
			ItemStack book = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
			Items.ENCHANTED_BOOK.addEnchantment(book, new EnchantmentData(Enchantments.lumbering, 1));

			if (event.getLeft() == null || event.getRight() == null)
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
					event.setOutput(null);
			}
		}
	}
}