package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundTweaks
{
	@SideOnly(Side.CLIENT)
	public static class VillagerTweak
	{
		static final VillagerTweak instance = new VillagerTweak();

		public static VillagerTweak getInstance()
		{
			return instance;
		}

		@SubscribeEvent
		public void registerTweak(SoundSourceEvent event)
		{
			if (event.name != null)
				if (event.name.contains("mob.villager"))
					event.setCanceled(true);
		}
	}
}
