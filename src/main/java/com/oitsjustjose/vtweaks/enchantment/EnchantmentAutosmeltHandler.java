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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentAutosmeltHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BlockEvent.HarvestDropsEvent event)
	{
		EntityPlayer player = event.getHarvester();
		if (player == null || player.getHeldItemMainhand() == null)
			return;

		World world = event.getWorld();

		ItemStack heldItem = player.getHeldItemMainhand();
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.autosmelt, heldItem);

		if (autosmeltLevel > 0)
		{
			ListIterator<ItemStack> iterator = event.getDrops().listIterator();

			while (iterator.hasNext())
			{
				ItemStack temp = iterator.next().copy();
				ItemStack newDrop = FurnaceRecipes.instance().getSmeltingResult(temp.copy());

				if (newDrop != null)
				{
					newDrop = newDrop.copy();
					newDrop.stackSize = temp.stackSize;
					if (event.getFortuneLevel() > 0 && shouldFortuneSmelt(temp))
						newDrop.stackSize *= world.rand.nextInt(event.getFortuneLevel() + 1) + 1;

					iterator.set(newDrop);
					spawnXP(event.getWorld(), event.getPos(), newDrop.copy());
				}
			}
		}

	}

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

	boolean shouldFortuneSmelt(ItemStack stack)
	{
		if (!VTweaks.config.autosmeltFortuneInteraction)
			return false;
		
		if (stack.getItem() instanceof ItemBlock)
		{
			Block block = Block.getBlockFromItem(stack.getItem());
			if (block.getRegistryName().toString().toLowerCase().contains("ore"))
				return true;
			else
				for (String s : VTweaks.config.autosmeltOverrides)
					if (block.getRegistryName().toString().toLowerCase().contains(s))
						return true;
		}
		return false;
	}
}