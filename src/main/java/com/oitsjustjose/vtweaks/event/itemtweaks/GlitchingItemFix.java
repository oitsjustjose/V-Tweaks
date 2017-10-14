package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.entity.Entity;
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
		if (!Config.getInstance().enableGlitchingItemFix)
			return;

		if (event.getTarget() == null || event.getEntityPlayer() == null)
			return;

		Entity entity = event.getTarget();
		EntityPlayer player = event.getEntityPlayer();

		if (entity instanceof EntityPainting)
		{
			if (!player.inventory.addItemStackToInventory(new ItemStack(Items.PAINTING, 1)))
				player.world.spawnEntity(HelperFunctions.createItemEntity(event.getEntity().world, event.getEntity().getPosition(), Items.PAINTING));
			entity.setDead();
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
				if (jukebox.getRecord() == ItemStack.EMPTY)
					return;

				ItemStack recordStack = jukebox.getRecord().copy();

				world.playEvent(1010, event.getPos(), 0);
				world.playRecord(event.getPos(), null);
				jukebox.setRecord(ItemStack.EMPTY);

				world.spawnEntity(HelperFunctions.createItemEntity(event.getEntity().world, event.getEntity().getPosition(), recordStack));
			}
		}
	}
}