package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

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

public class GlitchingItemFix
{
	/*
	 * Idea borrowed from maruohon Execution is completely different and unique, however
	 */
	@SubscribeEvent
	public void registerItemFixes(AttackEntityEvent event)
	{
		if (!VTweaks.config.enableGlitchingItemFix)
			return;

		if (event.getTarget() == null || event.getEntityPlayer() == null)
			return;

		Entity entity = event.getTarget();
		EntityPlayer player = event.getEntityPlayer();

		if (entity instanceof EntityPainting)
		{
			EntityItem paintingItemEntity = HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(), event.getEntity().getPosition(), Items.PAINTING);

			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.PAINTING, 1)))
				player.worldObj.spawnEntityInWorld(paintingItemEntity);
			entity.setDead();
		}

		// Ensures compatibility with TiCon
		if (entity instanceof EntityItemFrame && !(entity.getClass().getName().contains("tconstruct")))
		{

			EntityItemFrame frame = (EntityItemFrame) entity;
			ItemStack framedStack = frame.getDisplayedItem();

			if (framedStack == null)
			{
				if (!player.inventory.addItemStackToInventory(new ItemStack(Items.ITEM_FRAME, 1)))
					player.worldObj.spawnEntityInWorld(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(), event.getEntity().getPosition(), Items.ITEM_FRAME));
				entity.setDead();
			}
			else
			{
				if (!player.inventory.addItemStackToInventory(framedStack))
				{
					player.worldObj.spawnEntityInWorld(HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(), event.getEntity().getPosition(), framedStack));
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

				EntityItem record = HelperFunctions.createItemEntity(event.getEntity().getEntityWorld(), event.getEntity().getPosition(), recordStack);
				world.spawnEntityInWorld(record);
			}
		}
	}
}