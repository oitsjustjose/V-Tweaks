package com.oitsjustjose.VTweaks.Events.BlockTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CropHelper
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;

		IBlockState cropState = event.world.getBlockState(event.pos);
		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		Block harvestable = cropState.getBlock();
		int harvestableMeta = harvestable.getMetaFromState(cropState);
		int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(1);
		ItemStack dropStack = new ItemStack(harvestable.getItemDropped(cropState, event.world.rand, Short.MAX_VALUE), dropRate);

		boolean flag = CropHelperModSupport.registerExU(event);
		boolean pamCrop = harvestable.getClass().getName().startsWith("com.pam.harvestcraft.BlockPamCrop");
		CropHelperModSupport.registerNatura(event);
		CropHelperModSupport.registerWitchery(event);

		if (harvestable instanceof BlockCrops && harvestableMeta >= 7 && !flag && !pamCrop)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), dropStack);
				world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				world.spawnEntityInWorld(droppedItem);
			}
		}

		// Handles cocoa beans!
		if (harvestable instanceof BlockCocoa && harvestableMeta >= 8)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				int dropQty = 1 + world.rand.nextInt(3);
				EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), new ItemStack(Items.dye, dropQty, 3));
				world.setBlockState(event.pos, harvestable.getStateFromMeta(harvestableMeta - 8), 2);
				world.spawnEntityInWorld(droppedItem);
			}
		}

		// Handles Netherwart
		if (harvestable instanceof BlockNetherWart && harvestableMeta >= 3)
		{
			event.entityPlayer.swingItem();
			if (!world.isRemote)
			{
				int dropQty = 2 + (world.rand.nextInt(2) + 1);
				EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), new ItemStack(Items.nether_wart, dropQty));
				world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				world.spawnEntityInWorld(droppedItem);
			}
		}
	}
}