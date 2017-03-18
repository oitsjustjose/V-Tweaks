package com.oitsjustjose.vtweaks.util;

import java.util.List;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes
{
	public static void registerRecipes()
	{
		if (VTweaks.config.enableDisenchantRecipes && !(Loader.isModLoaded("Botania") || Loader.isModLoaded("ThaumicTinkerer")))
		{
			CraftingManager.getInstance().getRecipeList().add(new DisenchantRecipes(Items.PAPER));
			RecipeSorter.register("VTweaks:disenchanting", DisenchantRecipes.class, Category.SHAPELESS, "");
		}
	}

	public static void removeRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i) != null)
			{
				ItemStack recipeResult = recipes.get(i).getRecipeOutput();

				if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
					recipes.remove(i--);
			}
		}
	}

	@SubscribeEvent
	public void registerEvent(AnvilUpdateEvent event)
	{
		registerBookRecipes(event);

		if (VTweaks.config.enableRecipeHorseArmor)
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

	public void registerBookRecipes(AnvilUpdateEvent event)
	{
		if (VTweaks.config.hypermendingID > 0)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.hypermending);

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
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.autosmelt);
			
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

		if (VTweaks.config.stepboostID > 0)
		{
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.stepboost);

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
			ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.lumbering);

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

	public static class DisenchantRecipes implements IRecipe
	{
		Item toCombineWith;

		public DisenchantRecipes(Item item)
		{
			toCombineWith = item;
		}

		@Override
		public boolean matches(InventoryCrafting invCraft, World world)
		{
			boolean paperItem = false;
			boolean enchantedItem = false;

			for (int i = 0; i < invCraft.getSizeInventory(); i++)
			{
				ItemStack stack = invCraft.getStackInSlot(i);
				if (stack != null)
					if (stack.isItemEnchanted() && !enchantedItem)
						enchantedItem = true;
					else if (stack.getItem() == Items.PAPER && !paperItem)
						paperItem = true;
					else
						return false;
			}
			return enchantedItem && paperItem;
		}

		@Override
		public ItemStack getCraftingResult(InventoryCrafting invCraft)
		{
			ItemStack stackToClear = null;
			for (int i = 0; i < invCraft.getSizeInventory(); i++)
			{
				// tests the slots to see if there is an item and it's enchanted
				ItemStack testFor = invCraft.getStackInSlot(i);
				if (testFor != null && testFor.isItemEnchanted())
				{
					stackToClear = testFor.copy();
					break;
				}
			}

			if (stackToClear == null)
				return null;

			NBTTagCompound compound = (NBTTagCompound) stackToClear.getTagCompound().copy();
			compound.removeTag("ench");
			stackToClear.setTagCompound(compound);
			return stackToClear;
		}

		@Override
		public int getRecipeSize()
		{
			return 10;
		}

		@Override
		public ItemStack getRecipeOutput()
		{
			return null;
		}

		@Override
		public ItemStack[] getRemainingItems(InventoryCrafting invCraft)
		{
			ItemStack[] ret = new ItemStack[9];

			for (int i = 0; i < invCraft.getSizeInventory(); i++)
				if (invCraft.getStackInSlot(i) != null)
					if (invCraft.getStackInSlot(i).getItem() != Items.PAPER && !invCraft.getStackInSlot(i).isItemEnchanted())
						ret[i] = invCraft.getStackInSlot(i);

			return ret;
		}
	}
}