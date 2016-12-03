package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Iterator;
import java.util.List;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CropHelper
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerVanilla(RightClickBlock event)
	{
		if (event.getWorld().getBlockState(event.getPos()) == null || event.getEntityPlayer() == null || event.getHand() != EnumHand.MAIN_HAND)
			return;

		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block harvestable = state.getBlock();

		// Checks the class blacklist from the config
		for (String blackList : VTweaks.config.cropHarvestBlacklist)
			if (harvestable.getClass().getName().toLowerCase().contains(blackList.toLowerCase()))
				return;

		if (harvestable instanceof BlockCrops)
		{
			if (harvestGenericCrop(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
			{
				event.setCanceled(true);
			}
		}
		else if (harvestable instanceof BlockNetherWart)
		{
			if (harvestNetherWart(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
			{
				event.setCanceled(true);
			}
		}
		else if (harvestable instanceof BlockCocoa)
		{
			if (harvestCocoaPod(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
			{
				event.setCanceled(true);
			}
		}
	}

	public boolean harvestGenericCrop(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		BlockCrops crop = (BlockCrops) state.getBlock();
		if (crop.isMaxAge(state))
		{
			if (!world.isRemote)
			{
				final List<ItemStack> drops = crop.getDrops(world, pos, state, 0);
				final Item mainDrop = crop.getItemDropped(state, world.rand, 0);

				// An iterator to remove a seed or potato / carrot
				if (mainDrop != null)
				{
					Iterator<ItemStack> iter = drops.iterator();
					while (iter.hasNext())
					{
						final ItemStack next = iter.next();
						if (next.getItem() != mainDrop || next.getItem() == Items.POTATO || next.getItem() == Items.CARROT)
						{
							iter.remove();
							break;
						}
					}
				}
				// Drops all items remaining in the drop list
				for (ItemStack i : drops)
					dropItem(world, player.getPosition(), i);
				// Reverts crop to freshly planted state
				world.setBlockState(pos, crop.withAge(0));
			}
			player.swingArm(EnumHand.MAIN_HAND);
			return true;
		}
		return false;
	}

	public boolean harvestNetherWart(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		BlockNetherWart wart = (BlockNetherWart) state.getBlock();
		final List<ItemStack> drops = wart.getDrops(world, pos, state, 0);
		// This is how I'm determining the crop is grown
		if (drops.size() > 1)
		{
			if (!world.isRemote)
			{
				// Removes initial drop item, because it should cost one to be planted
				drops.remove(0);
				// Drops all items remaining in the drop list
				for (ItemStack i : drops)
					dropItem(world, player.getPosition(), i);
				// Reverts crop to freshly planted state
				world.setBlockState(pos, wart.getDefaultState());
			}
			player.swingArm(EnumHand.MAIN_HAND);
			return true;
		}
		return false;
	}

	public boolean harvestCocoaPod(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		BlockCocoa cocoa = (BlockCocoa) state.getBlock();
		final List<ItemStack> drops = cocoa.getDrops(world, pos, state, 0);
		// This is how I'm determining the crop is grown
		if (drops.size() > 1)
		{
			if (!world.isRemote)
			{
				// Removes initial drop item, because it should cost one to be planted
				drops.remove(0);
				// Drops all items remaining in the drop list
				for (ItemStack i : drops)
					dropItem(world, player.getPosition(), i);
				// Reverts crop to freshly planted state
				world.setBlockState(pos, state.withProperty(BlockCocoa.AGE, Integer.valueOf(0)));
			}
			player.swingArm(EnumHand.MAIN_HAND);
			return true;
		}
		return false;
	}

	public void dropItem(World world, BlockPos pos, ItemStack itemstack)
	{
		EntityItem drop = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack.copy());
		world.spawnEntity(drop);
	}
}
