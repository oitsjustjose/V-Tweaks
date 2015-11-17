package com.oitsjustjose.VTweaks.Tweaks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

/*
 * Fixes tool efficiencies. Why mojang, why not fix this yourself?? >_>
 */

public class BlockTweaks
{
	@SubscribeEvent
	public void registerEvent(PlayerEvent.BreakSpeed event)
	{
		Block block = event.block;
		ItemStack stack = event.entityPlayer.getHeldItem();
		if (block == null || stack == null)
			return;

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
		}
		if (stack.getItem() instanceof ItemPickaxe)
		{
			// Glass Block Harvest Fixer
			if (block.getMaterial() == Material.glass)
				event.newSpeed = event.originalSpeed * 4;
			// Packed Ice Harvest Fixer
			if (block == Blocks.packed_ice)
				event.newSpeed = event.originalSpeed * 4;
		}
		if (stack.getItem() instanceof ItemShears)
		{
			if (block == Blocks.hay_block)
				event.newSpeed = event.originalSpeed * 4;
		}
	}
}