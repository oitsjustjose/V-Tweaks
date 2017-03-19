package com.oitsjustjose.vtweaks.event.blocktweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CakeTweak
{
	@SubscribeEvent
	public void registerTweak(BlockEvent.HarvestDropsEvent event)
	{
		// Checks if feature is enabled
		if (!VTweaks.config.enableCakeTweak)
			return;

		Block block = event.getState().getBlock();
		if (event.getHarvester() != null && block instanceof BlockCake && block.getMetaFromState(event.getState()) == 0)
		{
			event.getDrops().clear();
			event.getDrops().add(new ItemStack(Items.CAKE));
		}
	}
}