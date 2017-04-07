package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Iterator;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LossPrevention
{
	@SubscribeEvent
	public void registerLavaTweak(HarvestDropsEvent event)
	{
		// Checks to see if feature is enabled
		if (!VTweaks.config.enableLavaLossPrevention)
			return;
		// Confirming that player exists
		if (event.getHarvester() == null || event.getState() == null || event.getState().getBlock() == null)
			return;

		EntityPlayer player = event.getHarvester();

		if (shouldPreventLoss(event.getState()) && dangerousPos(event.getWorld(), event.getPos(), Blocks.LAVA, Blocks.FLOWING_LAVA))
		{
			Iterator<ItemStack> iter = event.getDrops().iterator();
			while (iter.hasNext())
			{
				ItemStack drop = iter.next().copy();
				if (!event.getWorld().isRemote)
				{
					if (!player.inventory.addItemStackToInventory(drop))
					{
						event.getWorld().spawnEntity(HelperFunctions.createItemEntity(event.getWorld(), player.getPosition(), drop));
					}
				}
				iter.remove();
			}
		}
	}

	@SubscribeEvent
	public void registerCactusTweak(HarvestDropsEvent event)
	{
		// Checks to see if feature is enabled
		if (!VTweaks.config.enableCactusLossPrevention)
			return;
		// Confirming that player exists
		if (event.getHarvester() == null || event.getState() == null || event.getState().getBlock() == null)
			return;

		EntityPlayer player = event.getHarvester();

		if (shouldPreventLoss(event.getState()) && dangerousPos(event.getWorld(), event.getPos(), Blocks.CACTUS))
		{
			Iterator<ItemStack> iter = event.getDrops().iterator();
			while (iter.hasNext())
			{
				ItemStack drop = iter.next().copy();
				if (!event.getWorld().isRemote)
				{
					if (!player.inventory.addItemStackToInventory(drop))
					{
						event.getWorld().spawnEntity(HelperFunctions.createItemEntity(event.getWorld(), player.getPosition(), drop));
					}
				}
				iter.remove();
			}
		}
	}

	private boolean dangerousPos(World world, BlockPos pos, Block... dangerBlocks)
	{
		for (Block b : dangerBlocks)
			if (world.getBlockState(pos.down()).getBlock() == b)
				return true;
		return false;
	}

	private boolean shouldPreventLoss(IBlockState state)
	{
		ItemStack compare = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		for (ItemStack i : VTweaks.config.lavaLossBlockList)
			if (i.getItem() == compare.getItem() && i.getMetadata() == compare.getMetadata())
				return true;
		
		// Special case for if it's a cactus; saves code
		return state.getBlock() == Blocks.CACTUS;
	}
}
