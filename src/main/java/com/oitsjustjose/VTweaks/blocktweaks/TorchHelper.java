package com.oitsjustjose.vtweaks.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TorchHelper
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.entityPlayer.getCurrentEquippedItem() == null)
			return;

		EntityPlayer player = event.entityPlayer;

		if (!(player.getHeldItem().getItem() instanceof ItemTool))
			return;

		if (event.action == event.action.RIGHT_CLICK_BLOCK)
		{
			if (player.inventory.hasItem(Item.getItemFromBlock(Blocks.torch)))
			{
				if (!event.world.getBlockState(event.pos).getBlock().isBlockSolid(event.world, event.pos, event.face))
					return;
				if (!event.world.getBlockState(event.pos).getBlock().isSideSolid(event.world, event.pos, event.face))
					return;

				// Bottom
				if (event.face == EnumFacing.DOWN)
				{
					if (event.world.isAirBlock(event.pos.down()))
						if (canPlaceAnywhereBelow(event.world, event.pos))
							placeTorchBelow(event.world, event.pos, player);
				}
				// Top
				else if (event.face == EnumFacing.UP)
				{
					if (event.world.isAirBlock(event.pos.up()))
					{
						event.world.setBlockState(event.pos.up(), Blocks.torch.getStateFromMeta(5), 2);
						player.swingItem();
						event.world.playSoundAtEntity(event.entityPlayer, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
						if (!player.capabilities.isCreativeMode)
							player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
					}
				}
				// North Side
				else if (event.face == EnumFacing.NORTH)
				{
					if (event.world.isAirBlock(event.pos.north()))
					{
						event.world.setBlockState(event.pos.north(), Blocks.torch.getStateFromMeta(4), 2);
						player.swingItem();
						event.world.playSoundAtEntity(event.entityPlayer, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
						if (!player.capabilities.isCreativeMode)
							player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
					}
				}
				// South Side
				else if (event.face == EnumFacing.SOUTH)
				{
					if (event.world.isAirBlock(event.pos.south()))
					{
						event.world.setBlockState(event.pos.south(), Blocks.torch.getStateFromMeta(3), 2);
						player.swingItem();
						event.world.playSoundAtEntity(event.entityPlayer, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
						if (!player.capabilities.isCreativeMode)
							player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
					}
				}
				// West Side
				else if (event.face == EnumFacing.WEST)
				{
					if (event.world.isAirBlock(event.pos.west()))
					{
						event.world.setBlockState(event.pos.west(), Blocks.torch.getStateFromMeta(2), 2);
						player.swingItem();
						event.world.playSoundAtEntity(event.entityPlayer, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
						if (!player.capabilities.isCreativeMode)
							player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
					}
				}
				// East Side
				else if (event.face == EnumFacing.EAST)
				{
					if (event.world.isAirBlock(event.pos.east()))
					{
						event.world.setBlockState(event.pos.east(), Blocks.torch.getStateFromMeta(1), 2);
						player.swingItem();
						event.world.playSoundAtEntity(event.entityPlayer, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
						if (!player.capabilities.isCreativeMode)
							player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
					}
				}

			}
		}
	}

	// Dammit, this used to be unnecessary D:<
	void placeTorchBelow(World world, BlockPos pos, EntityPlayer player)
	{
		if (world.getBlockState(pos.down(2)).getBlock().isSideSolid(world, pos.down(2), EnumFacing.UP))
			world.setBlockState(pos.down(), Blocks.torch.getDefaultState());
		else if (world.getBlockState(pos.down().north()).getBlock().isSideSolid(world, pos.down().north(), EnumFacing.SOUTH))
			world.setBlockState(pos.down(), Blocks.torch.getStateFromMeta(3), 2);
		else if (world.getBlockState(pos.down().south()).getBlock().isSideSolid(world, pos.down().south(), EnumFacing.NORTH))
			world.setBlockState(pos.down(), Blocks.torch.getStateFromMeta(4), 2);
		else if (world.getBlockState(pos.down().west()).getBlock().isSideSolid(world, pos.down().west(), EnumFacing.EAST))
			world.setBlockState(pos.down(), Blocks.torch.getStateFromMeta(1), 2);
		else if (world.getBlockState(pos.down().east()).getBlock().isSideSolid(world, pos.down().east(), EnumFacing.WEST))
			world.setBlockState(pos.down(), Blocks.torch.getStateFromMeta(2), 2);

		player.swingItem();
		world.playSoundAtEntity(player, Block.soundTypeWood.getPlaceSound(), 1.0F, 0.8F);
		if (!player.capabilities.isCreativeMode)
			player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
	}

	// Custom written to check around all surrounding blocks beneath the right-clicked block
	boolean canPlaceAnywhereBelow(World world, BlockPos pos)
	{
		if (world.getBlockState(pos.down(2)).getBlock().isSideSolid(world, pos.down(2), EnumFacing.UP))
			return true;
		if (world.getBlockState(pos.down().north()).getBlock().isSideSolid(world, pos.down().north(), EnumFacing.NORTH))
			return true;
		if (world.getBlockState(pos.down().south()).getBlock().isSideSolid(world, pos.down().south(), EnumFacing.SOUTH))
			return true;
		if (world.getBlockState(pos.down().east()).getBlock().isSideSolid(world, pos.down().east(), EnumFacing.EAST))
			return true;
		if (world.getBlockState(pos.down().west()).getBlock().isSideSolid(world, pos.down().west(), EnumFacing.WEST))
			return true;
		return false;
	}
}
