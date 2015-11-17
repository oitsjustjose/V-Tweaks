package com.oitsjustjose.VTweaks.Events;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CropHelper
{
	@SubscribeEvent
	public void registerEvent(PlayerInteractEvent event)
	{
		if (event.world.getBlock(event.x, event.y, event.z) == null)
			return;
		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		Block harvestable = world.getBlock(event.x, event.y, event.z);
		int harvestableMeta = world.getBlockMetadata(event.x, event.y, event.z);
		int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(2);
		ItemStack dropStack = new ItemStack(
				harvestable.getItemDropped(harvestableMeta, event.world.rand, Short.MAX_VALUE), dropRate);

		if (event.action == Action.RIGHT_CLICK_BLOCK)
		{
			// Mod Compatibility, and a flag check to make sure that ExU Ender
			// Lilly's don't get checked as a blockcrop too
			// Another flag has been added, pamCrop, to check that the block
			// isn't a Pam's HC Crop too, y'know, to be safe.
			boolean flag = CropHelperModSupport.registerExU(event);
			boolean pamCrop = harvestable.getClass().getName().startsWith("com.pam.harvestcraft.BlockPamCrop");
			CropHelperModSupport.registerNatura(event);
			CropHelperModSupport.registerWitchery(event);

			// Hopefully compatible with most everything else....
			if (harvestable instanceof BlockCrops && harvestableMeta >= 7 && !flag && !pamCrop)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, dropStack);
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
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
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(Items.dye, dropQty, 3));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, harvestableMeta - 8, 3);
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
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(Items.nether_wart, dropQty));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}
		}
	}
}