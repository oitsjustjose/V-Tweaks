package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentAutosmeltHandler
{
	/*
	 * This part of the code does the actual work for the enchantment Without this class, the enchantment exists, but does not actually do a single
	 * thing....
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BlockEvent.HarvestDropsEvent event)
	{
		EntityPlayer player = event.harvester;
		if (player == null || player.getCurrentEquippedItem() == null)
			return;

		World world = event.world;
		Block block = event.state.getBlock();

		ItemStack heldItem = player.getCurrentEquippedItem();
		int fortune = event.fortuneLevel;
		int qty = block.quantityDropped(event.state, fortune, event.world.rand);
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Config.autosmeltEnchantmentID, heldItem);

		if (autosmeltLevel > 0)
		{
			// The to-be-dropped item
			ItemStack newDrop = getSmelted(new ItemStack(block.getItemDropped(event.state, event.world.rand, fortune), qty, event.state.getBlock()
					.damageDropped(event.state)));

			// Checks to see that the block CAN be smelted. If it doesn't have a smelting result,
			// this class has absolutely no effect
			if (newDrop != null)
			{
				// Determines the new quantity to drop. Defaults at 1
				int newQty = 1;

				// But if it's an ore, we'll take fortune into account
				if ((qty > 1 || block.getUnlocalizedName().toLowerCase().contains("ore")) && fortune > 0)
					newQty *= (world.rand.nextInt(fortune + 1) + 1);

				// Prevents any other drops
				event.drops.clear();
				// Adds our new drop in. I don't know why I have to break it down into the 3
				// different parts instead of just passing it newDrop, but doing THAT makes
				// weird things happen with the dropped items
				event.drops.add(new ItemStack(newDrop.getItem(), newQty, newDrop.getItemDamage()));

				// XP SPAWNS HYPE
				int xpAmt = MathHelper.ceiling_float_int(FurnaceRecipes.instance().getSmeltingExperience(newDrop)); // Yes, I give extra ;)
				event.world.spawnEntityInWorld(new EntityXPOrb(event.world, (double) event.pos.getX(), event.pos.getY() + 0.5, (double) event.pos
						.getZ(), xpAmt));
				world.playSoundAtEntity(player, "fire.ignite", 0.4F, 1.0F); // A sound now. That's new. Heh. Congrats. Particles don't work.
			}
		}

	}

	ItemStack getSmelted(ItemStack input)
	{
		if (FurnaceRecipes.instance().getSmeltingResult(input) != null)
			return FurnaceRecipes.instance().getSmeltingResult(input);
		return null;
	}
}