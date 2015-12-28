package com.oitsjustjose.vtweaks.event;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StormTweak
{
	@SubscribeEvent
	public void registerTweak(WorldEvent event)
	{
		World world = event.world;
		WorldInfo wInfo = world.getWorldInfo();

		if (world.isRaining())
			if (wInfo.isThundering())
				wInfo.setThundering(false);
	}
}
