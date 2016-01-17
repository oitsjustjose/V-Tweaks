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
		if (item instanceof ItemDoor && item != Items.iron_door)
			return 600;
		else if (item == Item.getItemFromBlock(Blocks.wooden_button))
			return 300;
		else if (item == Items.sign)
			return 600;
		else if (item == Item.getItemFromBlock(Blocks.ladder))
			return 200;
		else if (item == Items.boat)
			return 1500;
		else
			return 0;
	}
}