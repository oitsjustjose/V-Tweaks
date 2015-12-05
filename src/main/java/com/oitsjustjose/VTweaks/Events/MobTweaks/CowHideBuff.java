package com.oitsjustjose.VTweaks.Events.MobTweaks;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CowHideBuff
{
	@SubscribeEvent
	public void registerTweak(LivingDropsEvent event)
	{
		if (event.entity != null && (event.entity instanceof EntityCow))
		{
			Random random = new Random();
			ItemStack leatherStack = new ItemStack(Items.leather, 1 + random.nextInt(3));
			EntityItem leather = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, leatherStack);
			event.drops.add(leather);
		}
	}
}