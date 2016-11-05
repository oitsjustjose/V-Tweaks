package com.oitsjustjose.vtweaks.event.itemtweaks;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

public class WoodItemFuelHandler implements IFuelHandler
{
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		Item item = fuel.getItem();
		if (item instanceof ItemDoor && item != Items.IRON_DOOR)
			return 600;
		else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
			return 300;
		else if (item == Items.SIGN)
			return 600;
		else if (item == Item.getItemFromBlock(Blocks.LADDER))
			return 200;
		else if (item == Items.BOAT)
			return 1500;
		else
			return 0;
	}
}