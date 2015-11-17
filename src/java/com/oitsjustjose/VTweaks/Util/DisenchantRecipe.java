package com.oitsjustjose.VTweaks.Util;

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

public class DisenchantRecipe implements IRecipe
{
	// The item to be combined with an enchanted item
	Item toCombineWith;

	public DisenchantRecipe(Item item)
	{
		toCombineWith = item;
	}

	// Checks to see if the input items match
	@Override
	public boolean matches(InventoryCrafting invCraft, World world)
	{
		// Flags for if I've found the paper or enchanted item yet
		boolean paperItem = false;
		boolean enchantedItem = false;

		for (int i = 0; i < invCraft.getSizeInventory(); i++)
		{
			ItemStack stack = invCraft.getStackInSlot(i);
			if (stack != null)
				// sets the flag to true if enchanted item is found
				if (stack.isItemEnchanted() && !enchantedItem)
					enchantedItem = true;
				// sets the flag to true if the paper is found
				else
					if (stack.getItem() == Items.paper && !paperItem)
						paperItem = true;
					// ends the function altogether if it does nothing at all
					else
						return false;
		}
		// returns the combination of the flags
		return enchantedItem && paperItem;
	}

	// Sets the result as an unenchanted version of the input
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

		// disenchants the itemstack
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
}