package com.oitsjustjose.vtweaks.event.itemtweaks;

import java.util.Random;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EggHatchHandler
{
	@SubscribeEvent
	public void registerEvent(ItemExpireEvent event)
	{
		if (event.entityItem == null || event.entityItem.getEntityItem().getItem() != Items.egg)
			return;

		Random random = new Random();
		if (random.nextInt(25) == 0)
		{
			event.setCanceled(true);
			World world = event.entity.getEntityWorld();
			EntityChicken chick = new EntityChicken(world);
			chick.setGrowingAge(-24000);
			chick.posX = event.entityItem.posX;
			chick.posY = event.entityItem.posY;
			chick.posZ = event.entityItem.posZ;
			world.spawnEntityInWorld(chick);
			event.entityItem.setDead();
		}
	}
}
