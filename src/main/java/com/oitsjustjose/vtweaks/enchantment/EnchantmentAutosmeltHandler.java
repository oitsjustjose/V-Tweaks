package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.BlockPos;
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
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Config.autosmeltID, heldItem);

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

				spawnXP(event.world, event.pos, new ItemStack(newDrop.getItem(), newQty, newDrop.getItemDamage()));
			}
		}

	}

	// This code is very similar to Tinkers', because I didn't want to try to compete as "better or worse" because of XP returned
	void spawnXP(World world, BlockPos pos, ItemStack itemstack)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int i = itemstack.stackSize;
		float f = FurnaceRecipes.instance().getSmeltingExperience(itemstack);
		int j;

		if (f == 0.0F)
			i = 0;
		else if (f < 1.0F)
		{
			j = MathHelper.floor_float((float) i * f);

			if (j < MathHelper.ceiling_float_int((float) i * f) && (float) Math.random() < (float) i * f - (float) j)
				++j;

			i = j;
		}

		while (i > 0)
		{
			j = EntityXPOrb.getXPSplit(i);
			i -= j;
			world.spawnEntityInWorld(new EntityXPOrb(world, x, y + 0.5, z, j));
		}
	}

	ItemStack getSmelted(ItemStack input)
	{
		if (FurnaceRecipes.instance().getSmeltingResult(input) != null)
			return FurnaceRecipes.instance().getSmeltingResult(input);
		return null;
	}
}