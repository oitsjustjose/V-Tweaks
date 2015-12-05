package com.oitsjustjose.VTweaks.Events.BlockTweaks;

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

		// Fixes blocks that don't have any blockHardness at all :|
		if (block.getBlockHardness(event.entityPlayer.getEntityWorld(), event.pos) <= 0.0F)
			block.setHardness(2.0F);

		if (stack.getItem() instanceof ItemAxe)
		{
			// Melon Block Harvest Fixer
			if (block == Blocks.melon_block)
				event.newSpeed = event.originalSpeed * 4;
			// Leaf Block Harvest Fixer
			if (block.getMaterial() == Material.leaves)
				event.newSpeed = event.originalSpeed * 6;
			if (block == Blocks.hay_block)
				event.newSpeed = event.originalSpeed * 4;
			if (block == Blocks.ladder)
				event.newSpeed = event.originalSpeed * 5;
		}

		if (stack.getItem() instanceof ItemPickaxe)
		{
			// Glass Block Harvest Fixer
			if (block.getMaterial() == Material.glass)
				event.newSpeed = event.originalSpeed * 5;
			// Packed Ice Harvest Fixer
			if (block == Blocks.packed_ice)
				event.newSpeed = event.originalSpeed * 5;
			// Mob / Entity Skull Fixer
			if (block instanceof BlockSkull)
				event.newSpeed = event.originalSpeed * 4;
			// Lever Fixer
			if (block instanceof BlockLever)
				event.newSpeed = event.originalSpeed * 4;
		}

		if (stack.getItem() instanceof ItemShears)
		{
			if (block == Blocks.hay_block)
				event.newSpeed = event.originalSpeed * 4;
		}
	}
}