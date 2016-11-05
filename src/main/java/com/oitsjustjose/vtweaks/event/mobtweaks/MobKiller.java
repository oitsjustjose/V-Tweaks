package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

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
		Entity toKill = event.getEntity();

		if (toKill != null && toKill instanceof EntityBat && VTweaks.config.noBats)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityPigZombie && VTweaks.config.noPigZombies)
			event.setResult(Result.DENY);

		if (toKill != null && toKill instanceof EntityGhast && VTweaks.config.noPigZombies)
			if (event.getWorld().rand.nextInt(100) < 95)
				event.setResult(Result.DENY);
	}

	@SubscribeEvent
	public void registerWitherTweak(EntityEvent event)
	{
		Entity toKill = event.getEntity();

		if (toKill != null && toKill instanceof EntityWither && VTweaks.config.noOverworldWither)
		{
			EntityWither wither = (EntityWither) toKill;
			if (wither.worldObj.provider.getDimension() == 0)
			{
				wither.setDead();
				if (!event.getEntity().worldObj.isRemote)
				{
					EntityItem soulSand = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(Blocks.SOUL_SAND, 2));
					EntityItem skulls = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(Items.SKULL, 1, 1).setStackDisplayName("No Withers in the Overworld"));

					event.getEntity().getEntityWorld().spawnEntityInWorld(soulSand);
					event.getEntity().getEntityWorld().spawnEntityInWorld(skulls);
				}
			}
		}
	}
}