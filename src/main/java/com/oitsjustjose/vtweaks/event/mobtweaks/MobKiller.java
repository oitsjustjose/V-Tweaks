package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;
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

	@SubscribeEvent
	public void registerWitherTweak(EntityEvent event)
	{
		Entity toKill = event.entity;

		if (toKill != null && toKill instanceof EntityWither && Config.noOverworldWither)
		{
			EntityWither wither = (EntityWither) toKill;
			if (wither.worldObj.provider.getDimensionId() == 0)
			{
				wither.setDead();
				if (!event.entity.worldObj.isRemote)
				{
					EntityItem soulSand = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Blocks.soul_sand, 2));
					EntityItem skulls = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.skull, 1, 1).setStackDisplayName("No Withers in the Overworld"));
					
					event.entity.getEntityWorld().spawnEntityInWorld(soulSand);
					event.entity.getEntityWorld().spawnEntityInWorld(skulls);
				}
			}
		}
	}
}