package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//Idea stolen from copygirl's old tweaks mod. Code is all original, but I liked this

public class FeatherPlucker
{
	@SubscribeEvent
	public void registerEvent(EntityInteractEvent event)
	{
		if (event.target == null || !(event.target instanceof EntityChicken))
			return;

		EntityChicken chicken = (EntityChicken) event.target;
		EntityPlayer player = event.entityPlayer;
		if (!chicken.isChild())
		{
			if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemShears)
			{
				if (!player.worldObj.isRemote && chicken.getGrowingAge() == 0)
				{
					EntityItem featherDrop = new EntityItem(player.worldObj, event.target.posX, event.target.posY, event.target.posZ, new ItemStack(Items.feather));
					player.worldObj.spawnEntityInWorld(featherDrop);
					chicken.attackEntityFrom(DamageSource.generic, 0.0F);
					chicken.setGrowingAge(10000); // Used for a cooldown timer, essentially
					if (!player.capabilities.isCreativeMode)
						player.getHeldItem().attemptDamageItem(1, player.getRNG());
				}
			}
		}
	}
}
