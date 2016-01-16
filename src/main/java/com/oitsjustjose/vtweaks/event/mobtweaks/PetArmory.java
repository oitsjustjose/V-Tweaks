package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PetArmory
{
	@SubscribeEvent
	public void registerEvent(EntityInteractEvent event)
	{
		if (event.target == null)
			return;

		if (event.target instanceof EntityTameable)
		{
			EntityTameable tameable = (EntityTameable) event.target;
			EntityPlayer player = event.entityPlayer;

			if (player.getHeldItem() != null && tameable.isTamed())
			{
				if (player.getHeldItem().getItem() == Items.iron_horse_armor)
				{
					for (int i = 0; i < 4; i++)
						tameable.setCurrentItemOrArmor(i, new ItemStack(Items.iron_leggings));
					--player.getHeldItem().stackSize;
				}
				else if (player.getHeldItem().getItem() == Items.golden_horse_armor)
				{
					for (int i = 0; i < 4; i++)
						tameable.setCurrentItemOrArmor(i, new ItemStack(Items.golden_leggings));
					--player.getHeldItem().stackSize;
				}
				else if (player.getHeldItem().getItem() == Items.diamond_horse_armor)
				{
					for (int i = 0; i < 4; i++)
						tameable.setCurrentItemOrArmor(i, new ItemStack(Items.diamond_leggings));
					--player.getHeldItem().stackSize;
				}
			}
		}
	}
}
