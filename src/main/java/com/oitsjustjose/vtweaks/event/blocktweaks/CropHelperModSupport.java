package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
// import com.pam.harvestcraft.BlockPamCrop;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CropHelperModSupport
{
	public static void registerWitchery(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("witchery"))
		{
			IBlockState cropState = event.world.getBlockState(event.pos);
			World world = event.world;
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.pos, cropState, 0);

			Block garlicCrop = GameRegistry.findBlock("witchery", "garlicplant");
			Block artichokeCrop = GameRegistry.findBlock("witchery", "artichoke");
			Block wolfsbaneCrop = GameRegistry.findBlock("witchery", "wolfsbane");
			Block belladonnaCrop = GameRegistry.findBlock("witchery", "belladonna");
			Block snowbellCrop = GameRegistry.findBlock("witchery", "snowbell");
			Block wormwoodCrop = GameRegistry.findBlock("witchery", "wormwood");

			if (garlicCrop != null && harvestable == garlicCrop && harvestableMeta == 5)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}

			if (artichokeCrop != null && harvestable == artichokeCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}

			if (wolfsbaneCrop != null && harvestable == wolfsbaneCrop && harvestableMeta == 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}

			if (belladonnaCrop != null && harvestable == belladonnaCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}

			if (snowbellCrop != null && harvestable == snowbellCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}

			if (wormwoodCrop != null && harvestable == wormwoodCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}
			}
		}
	}

	public static void registerNatura(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("Natura"))
		{
			IBlockState cropState = event.world.getBlockState(event.pos);
			World world = event.world;
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.pos, cropState, 0);

			Block barley = GameRegistry.findBlock("Natura", "N Crops");
			if (barley == null)
				return;
			if (harvestable == barley && harvestableMeta == 3)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
				}

			}
		}
	}

	public static boolean registerExU(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("ExtraUtilities"))
		{
			IBlockState cropState = event.world.getBlockState(event.pos);
			World world = event.world;
			Block harvestable = cropState.getBlock();
			Block enderLilly = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			List<ItemStack> drops = harvestable.getDrops(world, event.pos, cropState, 0);

			if (enderLilly != null && harvestable == enderLilly && harvestableMeta >= 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					for (ItemStack stack : drops)
					{
						EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(), stack);
						world.spawnEntityInWorld(droppedItem);
					}
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					return true;
				}
			}
		}
		return false;
	}
}