package com.oitsjustjose.vtweaks.event.itemtweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HangingItemFix
{
	/*
	 * Idea borrowed from maruohon Execution is completely different and unique, however
	 */
	@SubscribeEvent
	public void registerItemFixes(AttackEntityEvent event)
	{
		if (event.getTarget() == null || event.getEntityPlayer() == null)
			return;

		Entity entity = event.getTarget();
		EntityPlayer player = event.getEntityPlayer();

		if (entity instanceof EntityPainting)
		{
			EntityItem paintingItemEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(Items.PAINTING, 1));

			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.PAINTING, 1)))
				player.worldObj.spawnEntityInWorld(paintingItemEntity);
			entity.setDead();
		}

		// Ensures compatibility with TiCon
		if (entity instanceof EntityItemFrame && !(entity.getClass().getName().contains("tconstruct")))
		{
			EntityItem itemFrameItemEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, new ItemStack(Items.ITEM_FRAME, 1));
			EntityItemFrame frame = (EntityItemFrame) entity;
			ItemStack framedStack = frame.getDisplayedItem();

			if (framedStack == null)
			{
				if (!player.inventory.addItemStackToInventory(new ItemStack(Items.ITEM_FRAME, 1)))
					player.worldObj.spawnEntityInWorld(itemFrameItemEntity);
				entity.setDead();
			}
			else
			{
				EntityItem framedItemEntity = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, framedStack);
				if (!player.inventory.addItemStackToInventory(framedStack))
				{
					player.worldObj.spawnEntityInWorld(framedItemEntity);
					frame.setDisplayedItem(null);
				}
			}
		}
	}

	@SubscribeEvent
	public void registerBlockFixes(RightClickBlock event)
	{
		if (event.getWorld().getBlockState(event.getPos()) == null)
			return;

		World world = event.getWorld();
		Block block = world.getBlockState(event.getPos()).getBlock();

		if (block instanceof BlockJukebox)
		{
			TileEntityJukebox jukebox = (TileEntityJukebox) world.getTileEntity(event.getPos());
			if (jukebox != null && !world.isRemote)
			{
				if (jukebox.getRecord() == null)
					return;

				ItemStack recordStack = jukebox.getRecord().copy();

				jukebox.setRecord(null);
				world.playBroadcastSound(1005, event.getPos(), 0);
				world.playRecord(event.getPos(), (SoundEvent) null);

				EntityItem record = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, recordStack);
				world.spawnEntityInWorld(record);
			}
		}
	}
}