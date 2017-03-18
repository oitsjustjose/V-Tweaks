package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StormTweak
{
	@SubscribeEvent
	public void registerTweak(WorldEvent event)
	{
		// Checks to see if the feature is enabled
		if (!VTweaks.config.enableStormTweak)
			return;
		
		// Converts storms to regular rain
		if (event.getWorld().isRaining())
			if (event.getWorld().getWorldInfo().isThundering())
				event.getWorld().getWorldInfo().setThundering(false);
	}
}
