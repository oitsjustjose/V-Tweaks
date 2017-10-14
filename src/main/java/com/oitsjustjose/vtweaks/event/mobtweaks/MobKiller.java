package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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

		if (toKill != null && toKill instanceof EntityBat && Config.getInstance().disableBats)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityPigZombie && Config.getInstance().disablePigZombies)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityGhast && Config.getInstance().disablePigZombies)
			if (event.getWorld().rand.nextInt(100) < 95)
				event.setResult(Result.DENY);
	}

	@SubscribeEvent
	public void registerWitherTweak(EntityJoinWorldEvent event)
	{
		// Checks to see if feature is enabled
		if (!Config.getInstance().disableWitherOverworld)
			return;
		// Typecast checking
		if (!(event.getEntity() instanceof EntityWither))
			return;

		EntityWither wither = (EntityWither) event.getEntity();

		if (!event.getWorld().isRemote)
		{
			event.getWorld().spawnEntity(HelperFunctions.createItemEntity(event.getWorld(), event.getEntity().getPosition(), new ItemStack(Blocks.SOUL_SAND, 4)));
			event.getWorld().spawnEntity(HelperFunctions.createItemEntity(event.getWorld(), event.getEntity().getPosition(), new ItemStack(Items.SKULL, 3, 1)));
		}
		wither.setDead();
	}
}