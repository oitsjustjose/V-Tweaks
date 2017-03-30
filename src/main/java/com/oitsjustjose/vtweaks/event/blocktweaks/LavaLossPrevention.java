package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Iterator;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LavaLossPrevention
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerTweak(HarvestDropsEvent event)
	{
		// Checks to see if feature is enabled
		if (!VTweaks.config.enableLavaLossPrevention)
			return;
		// Confirming that player exists
		if (event.getHarvester() == null || event.getState() == null || event.getState().getBlock() == null)
			return;

		EntityPlayer player = event.getHarvester();

		// Checks if the block broken is what I consider "valuable"
		if (shouldPreventLoss(event.getState().getBlock(), event.getState().getBlock().getMetaFromState(event.getState())))
		{
			// Confirms if it's above lava
			if (isAboveLava(event.getWorld(), event.getPos()))
			{
				// Captures all drops
				Iterator<ItemStack> iter = event.getDrops().iterator();
				while (iter.hasNext())
				{
					ItemStack drop = iter.next().copy();
					if (!event.getWorld().isRemote)
					{
						// And attempts to put them in inventory
						if (!player.inventory.addItemStackToInventory(drop))
						{
							// Otherwise player's feet it is
							event.getWorld().spawnEntityInWorld(HelperFunctions.createItemEntity(event.getWorld(), player.getPosition(), drop));
						}
					}
					iter.remove();
				}
			}
		}
	}

	private boolean isAboveLava(World world, BlockPos pos)
	{
		return (world.getBlockState(pos.down()).getBlock() == Blocks.LAVA || world.getBlockState(pos.down()).getBlock() == Blocks.FLOWING_LAVA);
	}

	private boolean shouldPreventLoss(Block block, int meta)
	{
		return VTweaks.config.lavaLossBlockList.contains(new ItemStack(block, 1, meta));
	}
}
