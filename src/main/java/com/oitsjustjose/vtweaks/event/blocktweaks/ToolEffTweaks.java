package com.oitsjustjose.vtweaks.event.blocktweaks;

import com.oitsjustjose.vtweaks.VTweaks;

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

public class ToolEffTweaks
{
	@SubscribeEvent
	public void registerTweak(PlayerEvent.BreakSpeed event)
	{
		if(!VTweaks.config.enableToolEffTweaks)
			return;
		
		Block block = event.getState().getBlock();
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if (block == null || stack == null)
			return;

		if (stack.getItem() instanceof ItemAxe)
		{
			if (block == Blocks.MELON_BLOCK)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
			if (block.getDefaultState().getMaterial() == Material.LEAVES)
				event.setNewSpeed(event.getOriginalSpeed() * 6);
			if (block == Blocks.HAY_BLOCK)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
			if (block == Blocks.LADDER)
				event.setNewSpeed(event.getOriginalSpeed() * 5);
		}

		if (stack.getItem() instanceof ItemPickaxe)
		{
			if (block.getDefaultState().getMaterial() == Material.GLASS)
				event.setNewSpeed(event.getOriginalSpeed() * 5);
			if (block == Blocks.PACKED_ICE)
				event.setNewSpeed(event.getOriginalSpeed() * 5);
			if (block instanceof BlockSkull)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
			if (block instanceof BlockLever)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
			if (block.getDefaultState().getMaterial() == Material.PISTON)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
		}

		if (stack.getItem() instanceof ItemShears)
		{
			if (block == Blocks.HAY_BLOCK)
				event.setNewSpeed(event.getOriginalSpeed() * 4);
		}
	}
}