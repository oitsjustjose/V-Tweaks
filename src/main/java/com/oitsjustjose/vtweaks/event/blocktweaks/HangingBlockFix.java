package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*
 * Idea borrowed from maruohon
 * Execution is completely different and unique, however
 */

public class HangingBlockFix
{
	@SubscribeEvent
	public void registerTweak(AttackEntityEvent event)
	{

		if (event.target == null || event.entityPlayer == null)
			return;

		Entity entity = event.target;
		EntityPlayer player = event.entityPlayer;

		if (entity instanceof EntityPainting)
		{
			EntityItem paintingItemEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.painting, 1));

			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.painting, 1)))
				event.entityPlayer.worldObj.spawnEntityInWorld(paintingItemEntity);
			entity.setDead();
		}

		if (entity instanceof EntityItemFrame)
		{
			EntityItem itemFrameItemEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Items.item_frame, 1));
			EntityItemFrame frame = (EntityItemFrame) entity;
			ItemStack framedStack = frame.getDisplayedItem();

			if (framedStack == null)
			{
				if (!player.inventory.addItemStackToInventory(new ItemStack(Items.item_frame, 1)))
					event.entityPlayer.worldObj.spawnEntityInWorld(itemFrameItemEntity);
				entity.setDead();
			}
			else
			{
				EntityItem framedItemEntity = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, framedStack);
				if (!player.inventory.addItemStackToInventory(framedStack))
				{
					event.entityPlayer.worldObj.spawnEntityInWorld(framedItemEntity);
					frame.setDisplayedItem(null);
				}
			}
		}
	}
}