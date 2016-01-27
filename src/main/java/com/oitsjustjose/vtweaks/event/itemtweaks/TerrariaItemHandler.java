package com.oitsjustjose.vtweaks.event.itemtweaks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemTool;

public class TerrariaItemHandler
{
	public static void registerTweak()
	{
		for (int i = 0; i < Item.itemRegistry.getKeys().size(); i++)
			if (Item.getItemById(i) != null && (Item.getItemById(i) instanceof ItemTool || Item.getItemById(i) instanceof ItemArmor || Item.getItemById(i) instanceof ItemHoe))
				Item.getItemById(i).setMaxDamage(0);
	}
}