package com.oitsjustjose.vtweaks.enchantment;

import java.util.ListIterator;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(VTweaks.modConfig.autosmeltID, heldItem);

		if (autosmeltLevel > 0)
		{
			ListIterator<ItemStack> iterator = event.drops.listIterator();

			while (iterator.hasNext())
			{
				ItemStack temp = iterator.next().copy();
				ItemStack newDrop = FurnaceRecipes.instance().getSmeltingResult(temp.copy());

				if (newDrop != null)
				{
					newDrop = newDrop.copy();
					newDrop.stackSize = temp.stackSize;
					if (event.fortuneLevel > 0 && isOre(temp))
						newDrop.stackSize *= world.rand.nextInt(event.fortuneLevel + 1) + 1;

					iterator.set(newDrop);
					spawnXP(event.world, event.pos, newDrop.copy());
				}
			}
		}

	}

	/**
	 * This code is very similar to Tinkers', because I didn't want to try to compete as "better or worse" because of XP returned
	 * 
	 * @param world
	 *            worldObj to use
	 * @param pos
	 *            the BlockPos where the block broken is located
	 * @param itemstack
	 *            the ItemStack that is a result of the smelted block
	 */
	void spawnXP(World world, BlockPos pos, ItemStack itemstack)
	{
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int stackSize = itemstack.stackSize;
		float smeltingXP = FurnaceRecipes.instance().getSmeltingExperience(itemstack);
		int xpSplit;

		if (smeltingXP == 0.0F)
			stackSize = 0;
		else if (smeltingXP < 1.0F)
		{
			xpSplit = MathHelper.floor_float((float) stackSize * smeltingXP);

			if (xpSplit < MathHelper.ceiling_float_int((float) stackSize * smeltingXP) && (float) Math.random() < (float) stackSize * smeltingXP - (float) xpSplit)
				++xpSplit;

			stackSize = xpSplit;
		}

		while (stackSize > 0)
		{
			xpSplit = EntityXPOrb.getXPSplit(stackSize);
			stackSize -= xpSplit;
			world.spawnEntityInWorld(new EntityXPOrb(world, x, y + 0.5, z, xpSplit));
		}
	}

	/**
	 * @param block
	 *            Block to check if is an ore
	 * @return true if the block's unlocalized name contains "ore" or "resource" ("resource" is for IC2 Compat)
	 */
	boolean isOre(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemBlock)
		{
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block.getRegistryName().toLowerCase().contains("ore"))
				return true;
			else
				for (String s : VTweaks.modConfig.autosmeltOverrides)
					if (block.getRegistryName().toLowerCase().contains(s))
						return true;
		}
		return false;
	}
}