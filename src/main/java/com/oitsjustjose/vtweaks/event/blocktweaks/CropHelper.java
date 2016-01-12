package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CropHelper
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null || event.entityPlayer.getHeldItem() != null)
			return;

		IBlockState cropState = event.world.getBlockState(event.pos);
		World world = event.world;
		Block harvestable = cropState.getBlock();
		int harvestableMeta = harvestable.getMetaFromState(cropState);
		List<ItemStack> drops = harvestable.getDrops(world, event.pos, cropState, 0);

		boolean enderLilly = CropHelperModSupport.registerExU(event);
		boolean pamCrop = harvestable.getClass().getName().startsWith("com.pam.harvestcraft.BlockPamCrop");
		boolean resourcefulCrops = harvestable.getClass().getName().startsWith("tehnut.resourceful.crops.block.BlockRCrop");
		CropHelperModSupport.registerNatura(event);
		CropHelperModSupport.registerWitchery(event);

		// Handles most crops
		if (harvestable instanceof BlockCrops && harvestableMeta >= 7 && !enderLilly && !pamCrop && !resourcefulCrops)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
			}
		}

		// Handles cocoa beans!
		if (harvestable instanceof BlockCocoa && harvestableMeta >= 8)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.pos, harvestable.getStateFromMeta(harvestableMeta - 8), 2);
			}
		}

		// Handles Netherwart
		if (harvestable instanceof BlockNetherWart && harvestableMeta >= 3)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
			}
		}
	}
}
