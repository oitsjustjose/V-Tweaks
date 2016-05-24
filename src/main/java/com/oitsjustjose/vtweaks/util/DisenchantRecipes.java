package com.oitsjustjose.vtweaks.util;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/*
 * Gotta be honest, couldn't have done this without Vazkii's code.
 * I learned a lot here, and I'll refer to this code plenty I'm sure,
 * but I have never done anything with IRecipe before. Thanks for
 * open-sourcing your mods Vaz! <3
 *
 * @Author: Vazkii
 * See https://github.com/Thaumic-Tinkerer/
 */

public class DisenchantRecipes implements IRecipe
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