package com.oitsjustjose.vtweaks.blocktweaks;

import net.minecraft.init.Items;

public class StackTweaks
{
	public static void registerTweaks()
	{
		Items.boat.setMaxStackSize(64);
		Items.minecart.setMaxStackSize(64);
		Items.furnace_minecart.setMaxStackSize(64);
		Items.chest_minecart.setMaxStackSize(64);
		Items.hopper_minecart.setMaxStackSize(64);
		Items.tnt_minecart.setMaxStackSize(64);
		Items.command_block_minecart.setMaxStackSize(64);
		Items.cake.setMaxStackSize(64);
		Items.snowball.setMaxStackSize(64);
		Items.egg.setMaxStackSize(64);
		Items.ender_pearl.setMaxStackSize(64);
		Items.enchanted_book.setMaxStackSize(64);
		Items.saddle.setMaxStackSize(64);
		Items.bed.setMaxStackSize(64);
	}
}