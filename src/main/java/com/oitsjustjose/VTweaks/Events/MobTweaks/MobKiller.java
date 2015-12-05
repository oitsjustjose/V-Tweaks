package com.oitsjustjose.VTweaks.Events.MobTweaks;

import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobKiller
{
	@SubscribeEvent
	public void registerTweak(LivingSpawnEvent event)
	{
		Entity toKill = event.entity;
		// Checks if mob is a bat, and no bats are enabled.
		if (toKill != null && toKill instanceof EntityBat && Config.noBats)
			event.setResult(Result.DENY);

		// Checks if mob is a Pig Zombie, and no Pig Zombies are enabled.
		if (toKill != null && toKill instanceof EntityPigZombie && Config.noPigZombies)
			event.setResult(Result.DENY);
		// Also decreases spawn rates of ghasts, which were out of control otherwise
		if (toKill != null && toKill instanceof EntityGhast && Config.noPigZombies)
			if (event.world.rand.nextInt(100) < 95)
				event.setResult(Result.DENY);
	}
}