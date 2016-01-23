package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class SheepDyeFix
{
	@SubscribeEvent
	public void registerEvent(EntityInteractEvent event)
	{
		if (event.target == null || !(event.target instanceof EntitySheep))
			return;

		EntitySheep sheep = (EntitySheep) event.target;
		EntityPlayer player = event.entityPlayer;
		
		if (!sheep.isChild())
			if (player.getHeldItem() != null && getDye(player.getHeldItem()) != -1)
				sheep.setFleeceColor(EnumDyeColor.byDyeDamage(getDye(player.getHeldItem())));
	}

	int getDye(ItemStack itemstack)
	{
		int[] ids = OreDictionary.getOreIDs(itemstack);
		for (int i = 0; i < ids.length; i++)
		{
			String name = OreDictionary.getOreName(ids[i]);
			for (int meta = 0; meta < 16; meta++)
			{
				int[] dyeIDs = OreDictionary.getOreIDs(new ItemStack(Items.dye, 1, meta));
				for (int j = 0; j < dyeIDs.length; j++)
					if (name.equalsIgnoreCase(OreDictionary.getOreName(dyeIDs[j])))
						return meta;
			}
		}
		return -1;
	}
}