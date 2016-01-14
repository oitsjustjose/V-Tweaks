package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VillagerTweak
{
	@SubscribeEvent
	public void registerTweak(SoundSourceEvent event)
	{
		if (event.name != null)
			if (event.name.contains("mob.villager"))
				event.setCanceled(true);
	}
}
