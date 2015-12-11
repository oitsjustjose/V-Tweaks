package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobDropBuffs
{
	@SubscribeEvent
	public void registerTweak(LivingDropsEvent event)
	{
		Random random = new Random();

		if (event.entity == null)
			return;

		if (event.entity instanceof EntityChicken && Config.featherBuff)
		{
			ItemStack dropStack = new ItemStack(Items.feather, 1 + random.nextInt(4));
			EntityItem dropEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, dropStack);
			event.drops.add(dropEntity);
		}

		else if (event.entity instanceof EntityCow && Config.hideBuff)
		{
			ItemStack dropStack = new ItemStack(Items.leather, 1 + random.nextInt(3));
			EntityItem dropEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, dropStack);
			event.drops.add(dropEntity);
		}

		else if (event.entity instanceof EntitySkeleton && Config.boneBuff)
		{
			ItemStack dropStack = new ItemStack(Items.bone, 1 + random.nextInt(2));
			EntityItem dropEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, dropStack);
			event.drops.add(dropEntity);
		}

		else if (event.entity instanceof EntitySquid && Config.sacBuff)
		{
			ItemStack dropStack = new ItemStack(Items.dye, 1 + random.nextInt(3));
			EntityItem dropEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, dropStack);
			event.drops.add(dropEntity);
		}

		else if (event.entity instanceof EntityEnderman && Config.enderpearlBuff)
		{
			ItemStack dropStack = new ItemStack(Items.ender_pearl, 1 + random.nextInt(1));
			EntityItem dropEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, dropStack);
			event.drops.add(dropEntity);
		}
	}
}