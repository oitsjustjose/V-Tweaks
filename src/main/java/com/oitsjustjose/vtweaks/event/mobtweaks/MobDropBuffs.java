package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.VTweaks;

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

		if (event.getEntity() == null)
			return;

		if (event.getEntity() instanceof EntityChicken && VTweaks.modConfig.featherBuff)
		{
			ItemStack dropStack = new ItemStack(Items.FEATHER, 1 + random.nextInt(4));
			EntityItem dropEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, dropStack);
			event.getDrops().add(dropEntity);
		}

		else if (event.getEntity() instanceof EntityCow && VTweaks.modConfig.hideBuff)
		{
			ItemStack dropStack = new ItemStack(Items.LEATHER, 1 + random.nextInt(3));
			EntityItem dropEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, dropStack);
			event.getDrops().add(dropEntity);
		}

		else if (event.getEntity() instanceof EntitySkeleton && VTweaks.modConfig.boneBuff)
		{
			ItemStack dropStack = new ItemStack(Items.BONE, 1 + random.nextInt(2));
			EntityItem dropEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, dropStack);
			event.getDrops().add(dropEntity);
		}

		else if (event.getEntity() instanceof EntitySquid && VTweaks.modConfig.sacBuff)
		{
			ItemStack dropStack = new ItemStack(Items.DYE, 1 + random.nextInt(3));
			EntityItem dropEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, dropStack);
			event.getDrops().add(dropEntity);
		}

		else if (event.getEntity() instanceof EntityEnderman && VTweaks.modConfig.enderpearlBuff)
		{
			ItemStack dropStack = new ItemStack(Items.ENDER_PEARL, 1 + random.nextInt(1));
			EntityItem dropEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, dropStack);
			event.getDrops().add(dropEntity);
		}
	}
}