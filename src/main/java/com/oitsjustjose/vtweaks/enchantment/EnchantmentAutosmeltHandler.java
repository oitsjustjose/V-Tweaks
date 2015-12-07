package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.util.Config;

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
			ItemStack newDrop = getSmelted(new ItemStack(block.getItemDropped(event.state, event.world.rand, fortune), qty, event.state.getBlock().damageDropped(event.state)));

			if (newDrop != null)
			{
				int newQty = 1;
				if ((qty > 1 || block.getUnlocalizedName().toLowerCase().contains("ore")) && fortune > 0)
					newQty *= (world.rand.nextInt(fortune + 1) + 1);

				event.drops.clear();
				event.drops.add(new ItemStack(newDrop.getItem(), newQty, newDrop.getItemDamage()));

				int xpAmt = event.world.rand.nextInt(MathHelper.ceiling_float_int(FurnaceRecipes.instance().getSmeltingExperience(newDrop)));
				event.world.spawnEntityInWorld(new EntityXPOrb(event.world, (double) event.pos.getX(), event.pos.getY() + 0.5, (double) event.pos.getZ(), xpAmt));
				world.playSoundAtEntity(player, "fire.ignite", 0.4F, 1.0F);
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