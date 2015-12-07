package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockTweaks
{
	@SubscribeEvent
	public void registerTweak(PlayerEvent.BreakSpeed event)
	{
		Block block = event.state.getBlock();
		ItemStack stack = event.entityPlayer.getHeldItem();
		if (block == null || stack == null)
			return;

		if (stack.getItem() instanceof ItemAxe)
		{
			if (block == Blocks.melon_block)
				event.newSpeed = event.originalSpeed * 4;
			if (block.getMaterial() == Material.leaves)
				event.newSpeed = event.originalSpeed * 6;
			if (block == Blocks.hay_block)
				event.newSpeed = event.originalSpeed * 4;
			if (block == Blocks.ladder)
				event.newSpeed = event.originalSpeed * 5;
		}

		if (stack.getItem() instanceof ItemPickaxe)
		{
			if (block.getMaterial() == Material.glass)
				event.newSpeed = event.originalSpeed * 5;
			if (block == Blocks.packed_ice)
				event.newSpeed = event.originalSpeed * 5;
			if (block instanceof BlockSkull)
				event.newSpeed = event.originalSpeed * 4;
			if (block instanceof BlockLever)
				event.newSpeed = event.originalSpeed * 4;
			if (block.getMaterial() == Material.piston)
				event.newSpeed = event.originalSpeed * 4;
		}

		if (stack.getItem() instanceof ItemShears)
		{
			if (block == Blocks.hay_block)
				event.newSpeed = event.originalSpeed * 4;
		}
	}
}