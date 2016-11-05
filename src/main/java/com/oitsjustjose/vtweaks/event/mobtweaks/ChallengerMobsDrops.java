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
		if (event.getEntity() != null && event.getEntity() instanceof EntityMob)
			if (isChallengerMob((EntityMob) event.getEntity()))
				for (int j = 0; j < 2; j++)
					event.getDrops().add(getItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ));
	}

	EntityItem getItem(World world, double x, double y, double z)
	{
		int RNG = world.rand.nextInt(VTweaks.config.challengerLootTable.size());
		ItemStack temp = VTweaks.config.challengerLootTable.get(RNG).copy();
		return new EntityItem(world, x, y, z, temp.copy());
	}

	boolean isChallengerMob(EntityMob entity)
	{
		String n = entity.getCustomNameTag().toLowerCase();
		if (n == null)
			return false;

		String[] preFixes = VTweaks.config.challengerMobs.clone();
		for (int i = 0; i < preFixes.length; i++)
			if (n.contains(preFixes[i].toLowerCase()))
				return true;
		return false;
	}
}