package com.oitsjustjose.vtweaks.mobtweaks;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SquidSacBuff
{
	@SubscribeEvent
	public void registerTweak(LivingDropsEvent event)
	{
		if (event.entity != null && event.entity instanceof EntitySquid)
		{
			Random random = new Random();
			ItemStack sacStack = new ItemStack(Items.dye, 1 + random.nextInt(3));
			EntityItem inkSac = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, sacStack);
			event.drops.add(inkSac);
		}
	}
}