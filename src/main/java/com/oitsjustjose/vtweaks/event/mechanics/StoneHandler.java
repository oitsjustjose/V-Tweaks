package com.oitsjustjose.vtweaks.event.mechanics;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StoneHandler
{
	@SubscribeEvent
	public void registerTweak(HarvestDropsEvent event)
	{
		if (event.harvester == null)
			return;
		if (event.isSilkTouching || Enchantments.hasAutoSmelt(event.harvester.getHeldItem()))
			return;

		if (event.state.getBlock() == Blocks.stone)
		{
			if (event.state.getBlock().getMetaFromState(event.state) == 0)
			{
				event.drops.clear();
				if (event.world.rand.nextInt(10 - event.fortuneLevel * 3) == 0)
					event.drops.add(new ItemStack(Items.flint));
				else
					event.drops.add(new ItemStack(Blocks.gravel));
			}
		}
	}
}
