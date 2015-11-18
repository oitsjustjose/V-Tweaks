package com.oitsjustjose.VTweaks.Events.MobTweaks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class BatKiller
{
	@SubscribeEvent
	public void registerTweak(EntityJoinWorldEvent event)
	{
		World world = event.world;
		
		if(world.isRemote)
			return;
		
		if(event.entity != null && event.entity instanceof EntityBat)
		{
			EntityBat bat = (EntityBat)event.entity;
			bat.setHealth(0.0F);			
			bat.setDead();
		}

	}
}