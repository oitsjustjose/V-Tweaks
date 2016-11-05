package com.oitsjustjose.vtweaks.event.blocktweaks;

import com.oitsjustjose.vtweaks.util.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Obsidianify
{
	@SubscribeEvent
	public void RegisterTweak(EntityJoinWorldEvent event)
	{
		if (event.getEntity() == null || !(event.getEntity() instanceof EntityItem))
			return;

		EntityItem itemEntity = (EntityItem) event.getEntity();

		if (!itemEntity.worldObj.isRemote)
		{
			if (itemEntity.getEntityItem() != null && Block.getBlockFromItem(itemEntity.getEntityItem().getItem()) == Blocks.OBSIDIAN)
			{
				EntityItem newItemEntity = new EntityItem(itemEntity.getEntityWorld(), itemEntity.posX, itemEntity.posY, itemEntity.posZ, itemEntity.getEntityItem())
				{
					@Override
					protected void dealFireDamage(int amount)
					{
						return;
					}
				}; 
				
				itemEntity.setDead();
				event.getWorld().spawnEntityInWorld(newItemEntity);
				LogHelper.info("Did the thing..?");
			}
		}
	}
}
