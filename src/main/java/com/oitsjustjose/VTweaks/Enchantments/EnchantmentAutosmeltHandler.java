package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentAutosmeltHandler
{
	/*
	 * This part of the code does the actual work for the enchantment Without
	 * this class, the enchantment exists, but does not actually do a single
	 * thing....
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void autosmelt(BlockEvent.HarvestDropsEvent event)
	{
		EntityPlayer player = event.harvester;
		if (player == null || player.getCurrentEquippedItem() == null)
			return;

		ItemStack heldItem = player.getCurrentEquippedItem();
		int fortune = event.fortuneLevel;
		int qty = event.block.quantityDropped(event.blockMetadata, fortune, event.world.rand);
		int metadata = event.blockMetadata;
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Config.autosmeltEnchantmentID, heldItem);
		World world = event.world;

		if (autosmeltLevel > 0)
		{
			ItemStack newDrop = getSmelted(
					new ItemStack(event.block.getItemDropped(metadata, event.world.rand, fortune), qty, metadata));
			if (newDrop != null)
			{
				int newQty;
				if ((qty > 1 || event.block.getUnlocalizedName().toLowerCase().contains("ore")) && fortune > 0)
					newQty = 1 + event.world.rand.nextInt(fortune + 1);
				else
					newQty = qty;

				event.drops.clear();
				event.drops.add(new ItemStack(newDrop.getItem(), newQty, newDrop.getItemDamage()));
			}
		}
	}

	ItemStack getSmelted(ItemStack input)
	{
		if (FurnaceRecipes.smelting().getSmeltingResult(input) != null)
			return FurnaceRecipes.smelting().getSmeltingResult(input);
		return null;
	}
}