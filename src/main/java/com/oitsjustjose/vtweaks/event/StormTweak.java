package com.oitsjustjose.vtweaks.event;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StormTweak
{
	@SubscribeEvent
	public void registerTweak(WorldEvent event)
	{
		if (event.getWorld().isRaining())
			if (event.getWorld().getWorldInfo().isThundering())
				event.getWorld().getWorldInfo().setThundering(false);
	}
}
