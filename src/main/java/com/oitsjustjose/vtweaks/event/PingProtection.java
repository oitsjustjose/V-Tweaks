package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.VTweaks;

import com.oitsjustjose.vtweaks.util.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PingProtection
{
	@SubscribeEvent
	public void registerTweak(LivingHurtEvent event)
	{
		// Check if feature is enabled
		if (!Config.getInstance().enablePingProtection)
			return;

		if (event.getEntity() instanceof EntityPlayerMP)
		{
			if (event.getSource() == DamageSource.DRAGON_BREATH || event.getSource() == DamageSource.WITHER || event.getSource() instanceof EntityDamageSource)
			{
				EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
				int ping = player.ping;
				float originalDamage = event.getAmount();
				float newDamage = originalDamage / (ping + 1);

				event.setAmount(newDamage);
			}
		}
	}
}
