package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
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

		if (toKill != null && toKill instanceof EntityBat && Config.noBats)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityPigZombie && Config.noPigZombies)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityGhast && Config.noPigZombies)
			if (event.world.rand.nextInt(100) < 95)
				event.setResult(Result.DENY);
	}
}