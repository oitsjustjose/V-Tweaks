package com.oitsjustjose.vtweaks.event.blocktweaks;

import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.HelperFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

public class CropHelper
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerVanilla(RightClickBlock event)
	{
		// Checks if feature is enabled
		if (!Config.getInstance().enableCropHelper)
			return;

		if (event.getWorld().getBlockState(event.getPos()) == null || event.getEntityPlayer() == null || event.getHand() != EnumHand.MAIN_HAND)
			return;

		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block harvestable = state.getBlock();

		// Checks the class blacklist from the config
		for (String blackList : Config.getInstance().cropHelperBlacklist)
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
				final NonNullList<ItemStack> drops = NonNullList.create();
				final Item mainDrop = crop.getItemDropped(state, world.rand, 0);
				crop.getDrops(drops, world, pos, state, 0);
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
		final NonNullList<ItemStack> drops = NonNullList.create();
		wart.getDrops(drops, world, pos, state, 0);
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
		final NonNullList<ItemStack> drops = NonNullList.create();
		cocoa.getDrops(drops, world, pos, state, 0);
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
		world.spawnEntity(HelperFunctions.createItemEntity(world, pos, itemstack));
	}
}
