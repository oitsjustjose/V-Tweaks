package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class CropHelper
{
	public boolean enderLilly = false;

	@SubscribeEvent
	public void registerVanilla(RightClickBlock event)
	{
		if (event.getWorld().getBlockState(event.getPos()) == null)
			return;

		IBlockState cropState = event.getWorld().getBlockState(event.getPos());
		World world = event.getWorld();
		Block harvestable = cropState.getBlock();
		int harvestableMeta = harvestable.getMetaFromState(cropState);

		boolean pamCrop = harvestable.getClass().getName().startsWith("com.pam.harvestcraft.BlockPamCrop");
		boolean resourcefulCrops = harvestable.getClass().getName().startsWith("tehnut.resourceful.crops.block.BlockRCrop");

		// Handles most crops
		if (harvestable instanceof BlockCrops && harvestableMeta >= 7 && !enderLilly && !pamCrop && !resourcefulCrops)
		{
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					ItemStack drop = stack.copy();
					if (drop.stackSize > 1)
						--drop.stackSize;
					EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
				event.setCanceled(true);
			}
		}

		// Handles beetroot. What the hell mojang, why not keep these BlockCrop...  :|
		if (harvestable instanceof BlockBeetroot && harvestableMeta >= 3 && !enderLilly && !pamCrop && !resourcefulCrops)
		{
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					ItemStack drop = stack.copy();
					if (drop.stackSize > 1)
						--drop.stackSize;
					EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
				event.setCanceled(true);
			}
		}
		
		// Handles cocoa beans!
		if (harvestable instanceof BlockCocoa && harvestableMeta >= 8)
		{
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					ItemStack drop = stack.copy();
					if (drop.stackSize > 1)
						--drop.stackSize;
					EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.getPos(), harvestable.getStateFromMeta(harvestableMeta - 8), 2);
				event.setCanceled(true);
			}
		}

		// Handles Netherwart
		if (harvestable instanceof BlockNetherWart && harvestableMeta >= 3)
		{
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			if (!world.isRemote)
			{
				for (ItemStack stack : drops)
				{
					ItemStack drop = stack.copy();
					if (drop.stackSize > 1)
						--drop.stackSize;
					EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
					world.spawnEntityInWorld(droppedItem);
				}
				world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void registerWitchery(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("witchery"))
		{
			if (event.getWorld().getBlockState(event.getPos()) == null)
				return;

			IBlockState cropState = event.getWorld().getBlockState(event.getPos());
			World world = event.getWorld();
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			Block garlicCrop = GameRegistry.findBlock("witchery", "garlicplant");
			Block artichokeCrop = GameRegistry.findBlock("witchery", "artichoke");
			Block wolfsbaneCrop = GameRegistry.findBlock("witchery", "wolfsbane");
			Block belladonnaCrop = GameRegistry.findBlock("witchery", "belladonna");
			Block snowbellCrop = GameRegistry.findBlock("witchery", "snowbell");
			Block wormwoodCrop = GameRegistry.findBlock("witchery", "wormwood");

			if (garlicCrop != null && harvestable == garlicCrop && harvestableMeta == 5)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}

			if (artichokeCrop != null && harvestable == artichokeCrop && harvestableMeta == 4)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}

			if (wolfsbaneCrop != null && harvestable == wolfsbaneCrop && harvestableMeta == 7)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}

			if (belladonnaCrop != null && harvestable == belladonnaCrop && harvestableMeta == 4)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}

			if (snowbellCrop != null && harvestable == snowbellCrop && harvestableMeta == 4)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}

			if (wormwoodCrop != null && harvestable == wormwoodCrop && harvestableMeta == 4)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void registerNatura(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("Natura"))
		{
			if (event.getWorld().getBlockState(event.getPos()) == null)
				return;

			IBlockState cropState = event.getWorld().getBlockState(event.getPos());
			World world = event.getWorld();
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			Block barley = GameRegistry.findBlock("Natura", "N Crops");
			if (barley == null)
				return;
			if (harvestable == barley && harvestableMeta == 3)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
				}

			}
		}
	}

	@SubscribeEvent
	public void registerExU(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("ExtraUtilities"))
		{
			if (event.getWorld().getBlockState(event.getPos()) == null)
				return;

			IBlockState cropState = event.getWorld().getBlockState(event.getPos());
			World world = event.getWorld();
			Block harvestable = cropState.getBlock();
			Block enderLilly = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.getPos(), cropState, 0);

			if (enderLilly != null && harvestable == enderLilly && harvestableMeta >= 7)
			{
				event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						ItemStack drop = stack.copy();
						if (drop.stackSize > 1)
							--drop.stackSize;
						EntityItem droppedItem = new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), drop);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.getPos(), harvestable.getDefaultState(), 2);
					event.setCanceled(true);
					this.enderLilly = true;
				}
			}
		}
	}
}
