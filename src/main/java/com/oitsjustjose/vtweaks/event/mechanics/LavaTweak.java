package com.oitsjustjose.vtweaks.event.mechanics;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LavaTweak
{
	@SubscribeEvent
	public void registerTweak(BlockEvent event)
	{
		World world = event.world;
		Block block = world.getBlockState(event.pos).getBlock();

		if (world.isRaining())
		{
			world.scheduleBlockUpdate(event.pos, block, 20, 1);

			if (world.canSeeSky(event.pos))
			{
				if (block == Blocks.flowing_lava)
				{
					if (block.getMetaFromState(event.state) == 0)
						world.setBlockState(event.pos, Blocks.obsidian.getDefaultState());
					else
						world.setBlockState(event.pos, Blocks.cobblestone.getDefaultState());
				}
				if (block == Blocks.lava)
					world.setBlockState(event.pos, Blocks.obsidian.getDefaultState());
			}
		}
	}
}