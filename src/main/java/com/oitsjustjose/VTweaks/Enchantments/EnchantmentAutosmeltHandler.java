package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class EnchantmentAutosmeltHandler
{
	/*
	 * This part of the code does the actual work for the enchantment Without this class, the enchantment exists, but
	 * does not actually do a single thing....
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BlockEvent.HarvestDropsEvent event)
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
			//The to-be-dropped item
			ItemStack newDrop = getSmelted(new ItemStack(event.block.getItemDropped(metadata, event.world.rand,
					fortune), qty, metadata));
			
			//Checks to see that the block CAN be smelted. If it doesn't have a smelting result, 
			//this class has absolutely no effect
			if (newDrop != null)
			{
				//Determines the new quantity to drop. Defaults at 1
				int newQty = 1;
				
				//But if it's an ore, we'll take fortune into account
				if ((qty > 1 || event.block.getUnlocalizedName().toLowerCase().contains("ore")) && fortune > 0)
					newQty *= (world.rand.nextInt(fortune + 1) + 1);
				
				//Prevents any other drops
				event.drops.clear();
				//Adds our new drop in. I don't know why I have to break it down into the 3
				//different parts instead of just passing it newDrop, but doing THAT makes
				//weird things happen with the dropped items
				event.drops.add(new ItemStack(newDrop.getItem(), newQty, newDrop.getItemDamage()));

				//XP SPAWNS HYPE
				int xpAmt = MathHelper.ceiling_float_int(FurnaceRecipes.smelting().func_151398_b(newDrop)); // Yes, I give xtra ;)
				event.world.spawnEntityInWorld(new EntityXPOrb(event.world, (double)event.x, event.y + 0.5, (double)event.z, xpAmt));
				world.playSoundAtEntity(player, "fire.ignite", 0.4F, 1.0F); //A sound now. That's new. Heh. Congrats. Particles don't work.
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