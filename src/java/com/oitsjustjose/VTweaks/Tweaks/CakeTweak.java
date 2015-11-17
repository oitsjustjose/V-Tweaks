package com.oitsjustjose.VTweaks.Tweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CakeTweak
{
	@SubscribeEvent
	public void registerEvent(BlockEvent.HarvestDropsEvent event)
	{
		Block block = event.block;
		if (event.harvester != null && block instanceof BlockCake && event.blockMetadata == 0)
		{
			event.drops.clear();
			event.drops.add(new ItemStack(Items.cake));
		}
	}
}