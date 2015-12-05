package com.oitsjustjose.VTweaks.Events.MobTweaks;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SkeletonBoneBuff
{
	@SubscribeEvent
	public void registerTweak(LivingDropsEvent event)
	{
		if (event.entity != null && event.entity instanceof EntitySkeleton)
		{
			Random random = new Random();
			ItemStack boneStack = new ItemStack(Items.bone, 1 + random.nextInt(2));
			ItemStack bonemealStack = new ItemStack(Items.dye, 3, 15);
			EntityItem bones = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, boneStack);
			EntityItem bonemeal = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, bonemealStack);
			event.drops.add(bones);
			event.drops.add(bonemeal);
		}
	}
}