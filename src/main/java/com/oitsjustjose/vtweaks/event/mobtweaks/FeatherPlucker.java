package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//Idea stolen from copygirl's old tweaks mod. Code is all original, but I liked this

public class FeatherPlucker
{
	@SubscribeEvent
	public void registerEvent(EntityInteract event)
	{
		if (event.getTarget() == null || !(event.getTarget() instanceof EntityChicken))
			return;

		EntityChicken chicken = (EntityChicken) event.getTarget();
		EntityPlayer player = event.getEntityPlayer();
		if (!chicken.isChild())
		{
			if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemShears)
			{
				if (!player.worldObj.isRemote && chicken.getGrowingAge() == 0)
				{
					EntityItem featherDrop = new EntityItem(player.worldObj, event.getTarget().posX, event.getTarget().posY, event.getTarget().posZ, new ItemStack(Items.FEATHER));
					player.worldObj.spawnEntityInWorld(featherDrop);
					chicken.attackEntityFrom(DamageSource.generic, 0.0F);
					chicken.setGrowingAge(10000); // Used for a cooldown timer, essentially
					if (!player.capabilities.isCreativeMode)
						player.getHeldItemMainhand().attemptDamageItem(1, player.getRNG());
				}
			}
		}
	}
}
