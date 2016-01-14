package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
		if (Config.disenchant && !(Loader.isModLoaded("Botania") || Loader.isModLoaded("ThaumicTinkerer")))
		{
			CraftingManager.getInstance().getRecipeList().add(new DisenchantRecipes(Items.paper));
			RecipeSorter.register("VTweaks:disenchanting", DisenchantRecipes.class, Category.SHAPELESS, "");
		}
	}
	
	

	@SubscribeEvent
	public void registerEvent(AnvilUpdateEvent event)
	{
		registerBookRecipes(event);

		if (Config.horseArmor)
			registerHorseArmorRecipes(event);
	}

	public void registerHorseArmorRecipes(AnvilUpdateEvent event)
	{
		if (event.left == null || event.right == null)
			return;

		Item left = event.left.getItem();
		Item right = event.right.getItem();

		boolean damaged = event.left.getItemDamage() > 0 || event.right.getItemDamage() > 0;

		if ((left == Items.diamond_leggings && right == Items.diamond_leggings) && !damaged)
		{
			event.cost = 35;
			event.output = new ItemStack(Items.diamond_horse_armor).setStackDisplayName("Large Dog Suit");
		}
		if ((left == Items.golden_leggings && right == Items.golden_leggings) && !damaged)
		{
			event.cost = 30;
			event.output = new ItemStack(Items.golden_horse_armor).setStackDisplayName("Medium Dog Suit");
		}
		if ((left == Items.iron_leggings && right == Items.iron_leggings) && !damaged)
		{
			event.cost = 20;
			event.output = new ItemStack(Items.iron_horse_armor).setStackDisplayName("Small Dog Suit");
		}
	}

	public void registerBookRecipes(AnvilUpdateEvent event)
	{
		if (Config.hypermendingID > 0)
		{
			ItemStack book = new ItemStack(Items.enchanted_book, 1, 0);
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantments.hyperMending, 1));

			if (event.left == null || event.right == null)
				return;

			if (event.left.getItem() == Items.writable_book && event.right.getItem() == Items.nether_star)
			{
				if (event.right.stackSize == 1)
				{
					event.cost = 30;
					event.output = book;
				}
				else
					event.output = null;
			}
		}

		if (Config.autosmeltID > 0)
		{
			ItemStack book = new ItemStack(Items.enchanted_book, 1, 0);
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantments.autosmelt, 1));

			if (event.left == null || event.right == null)
				return;

			if (event.left.getItem() == Items.writable_book && event.right.getItem() == Items.lava_bucket)
				if (event.right.stackSize == 1)
				{
					event.cost = 30;
					event.output = book;
				}
				else
					event.output = null;
		}

		if (Config.lumberingID > 0)
		{
			ItemStack book = new ItemStack(Items.enchanted_book, 1, 0);
			Items.enchanted_book.addEnchantment(book, new EnchantmentData(Enchantments.lumbering, 1));

			if (event.left == null || event.right == null)
				return;

			boolean damaged = event.right.getItemDamage() > 0;

			if (event.left.getItem() == Items.writable_book && event.right.getItem() == Items.golden_axe)
			{
				if (!damaged)
				{
					event.cost = 30;
					event.output = book;
				}
				else
					event.output = null;
			}
		}
	}
}