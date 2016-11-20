package com.oitsjustjose.vtweaks.util;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
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
	@Override
	public boolean matches(InventoryCrafting invCraft, World world)
	{
		boolean paperItem = false;
		boolean enchantedItem = false;

		for (int i = 0; i < invCraft.getSizeInventory(); i++)
		{
			ItemStack stack = invCraft.getStackInSlot(i);
			if (stack.getItem() != null)
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
		ItemStack stackToClear = new ItemStack((Item) null);
		for (int i = 0; i < invCraft.getSizeInventory(); i++)
		{
			// tests the slots to see if there is an item and it's enchanted
			ItemStack testFor = invCraft.getStackInSlot(i);
			if (testFor.getItem() != null && testFor.isItemEnchanted())
			{
				stackToClear = testFor.copy();
				break;
			}
		}

		if (stackToClear.getItem() == null)
			return new ItemStack((Item) null);

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
		return new ItemStack((Item) null);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting invCraft)
	{
		NonNullList<ItemStack> ret = NonNullList.func_191196_a();

		for (int i = 0; i < invCraft.getSizeInventory(); i++)
			if (invCraft.getStackInSlot(i).getItem() != null)
				if (invCraft.getStackInSlot(i).getItem() != Items.PAPER && !invCraft.getStackInSlot(i).isItemEnchanted())
					ret.set(i, invCraft.getStackInSlot(i));

		return ret;
	}
}