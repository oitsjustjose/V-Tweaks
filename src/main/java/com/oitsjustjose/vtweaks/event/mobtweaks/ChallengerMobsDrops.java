package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//This class is for adding special drops to my specially made mobs

public class ChallengerMobsDrops
{
	@SubscribeEvent
	public void registerEvent(LivingDropsEvent event)
	{
		if (event.entity != null && event.entity instanceof EntityMob)
			if (ChallengerMobs.isChallengerMob((EntityMob) event.entity))
				for (int j = 0; j < 2; j++)
					event.drops.add(getItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ));
	}

	EntityItem getItem(World world, double x, double y, double z)
	{
		int RNG = world.rand.nextInt(VTweaks.challengerLootTable.size());
		ItemStack temp = VTweaks.challengerLootTable.get(RNG).copy();

		//EntityItems are weird sometimes... so I did this instead.
		return new EntityItem(world, x, y, z, temp.copy());
	}
}