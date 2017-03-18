package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobKiller
{
	@SubscribeEvent
	public void registerTweak(LivingSpawnEvent event)
	{
		Entity toKill = event.getEntity();

		if (toKill != null && toKill instanceof EntityBat && VTweaks.config.disableBats)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityPigZombie && VTweaks.config.disablePigZombies)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityGhast && VTweaks.config.disablePigZombies)
			if (event.getWorld().rand.nextInt(100) < 95)
				event.setResult(Result.DENY);
	}

	@SubscribeEvent
	public void registerWitherTweak(EntityJoinWorldEvent event)
	{
		// Checks to see if feature is enabled
		if (!VTweaks.config.disableWitherOverworld)
			return;
		// Typecast checking
		if (!(event.getEntity() instanceof EntityWither))
			return;

		EntityWither wither = (EntityWither) event.getEntity();

		if (!event.getWorld().isRemote)
		{
			EntityItem sSand = HelperFunctions.createItemEntity(event.getWorld(), event.getEntity().getPosition(), new ItemStack(Blocks.SOUL_SAND, 4));
			EntityItem skulls = HelperFunctions.createItemEntity(event.getWorld(), event.getEntity().getPosition(), new ItemStack(Blocks.SKULL, 3, 1));

			event.getWorld().spawnEntityInWorld(sSand);
			event.getWorld().spawnEntityInWorld(skulls);
		}
		wither.setDead();
	}
}