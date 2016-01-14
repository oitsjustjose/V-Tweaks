package com.oitsjustjose.vtweaks.event;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StormTweak
{
	@SubscribeEvent
	public void registerTweak(WorldEvent event)
	{
		if (event.world.isRaining())
			if (event.world.getWorldInfo().isThundering())
				event.world.getWorldInfo().setThundering(false);
	}
}
