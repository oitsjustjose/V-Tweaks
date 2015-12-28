package com.oitsjustjose.vtweaks.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class LightningKiller
{
	@SubscribeEvent
	public void registerTweak(EntityEvent event)
	{
		Entity toKill = event.entity;
		if (toKill != null && toKill instanceof EntityLightningBolt)
		{
			EntityLightningBolt bolt = (EntityLightningBolt) toKill;
			bolt.setDead();
			bolt.worldObj.setLastLightningBolt(0);
			System.out.println("Kaboom");
//			event.setResult(Result.DENY);
		}
	}
}
