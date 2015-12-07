package com.oitsjustjose.vtweaks.event.blocktweaks;

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
		Block block = event.state.getBlock();
		if (event.harvester != null && block instanceof BlockCake && block.getDamageValue(event.world, event.pos) == 0)
		{
			event.drops.clear();
			event.drops.add(new ItemStack(Items.cake));
		}
	}
}