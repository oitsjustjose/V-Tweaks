package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
// import com.pam.harvestcraft.BlockPamCrop;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
			EntityPlayer player = event.entityPlayer;
			World world = event.world;
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(1);
			ItemStack dropStack = new ItemStack(harvestable.getItemDropped(cropState, event.world.rand, Short.MAX_VALUE), dropRate);

			Block garlicCrop = GameRegistry.findBlock("witchery", "garlicplant");
			Block artichokeCrop = GameRegistry.findBlock("witchery", "artichoke");
			Block wolfsbaneCrop = GameRegistry.findBlock("witchery", "wolfsbane");
			Block belladonnaCrop = GameRegistry.findBlock("witchery", "belladonna");
			Block snowbellCrop = GameRegistry.findBlock("witchery", "snowbell");
			Block wormwoodCrop = GameRegistry.findBlock("witchery", "wormwood");

			Item ingredient = GameRegistry.findItem("witchery", "ingredient");

			int artichoke = 69;
			int wolfsbane = 156;
			int belladonna = 21;
			int icyNeedle = 78;
			int wormwood = 111;

			if (garlicCrop != null && harvestable == garlicCrop && harvestableMeta == 5)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							dropStack);
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (artichokeCrop != null && harvestable == artichokeCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(ingredient, dropRate, artichoke));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (wolfsbaneCrop != null && harvestable == wolfsbaneCrop && harvestableMeta == 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(ingredient, dropRate, wolfsbane));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (belladonnaCrop != null && harvestable == belladonnaCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(ingredient, dropRate, belladonna));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (snowbellCrop != null && harvestable == snowbellCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem1 = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							dropStack);
					EntityItem droppedItem2 = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(ingredient, 1, icyNeedle));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem1);
					if (world.rand.nextInt(8) == 1)
						world.spawnEntityInWorld(droppedItem2);
				}
			}

			if (wormwoodCrop != null && harvestable == wormwoodCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(ingredient, dropRate, wormwood));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}
			}
		}
	}

	public static void registerNatura(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("Natura"))
		{
			IBlockState cropState = event.world.getBlockState(event.pos);
			EntityPlayer player = event.entityPlayer;
			World world = event.world;
			Block harvestable = cropState.getBlock();
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(2);
			ItemStack dropStack = new ItemStack(harvestable.getItemDropped(cropState, event.world.rand, Short.MAX_VALUE), dropRate);

			Block barley = GameRegistry.findBlock("Natura", "N Crops");
			if (barley == null)
				return;
			if (harvestable == barley && harvestableMeta == 3)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							dropStack);
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
				}

			}
		}
	}

	public static boolean registerExU(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("ExtraUtilities"))
		{
			IBlockState cropState = event.world.getBlockState(event.pos);
			EntityPlayer player = event.entityPlayer;
			World world = event.world;
			Block harvestable = cropState.getBlock();
			Block enderLilly = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
			int harvestableMeta = harvestable.getMetaFromState(cropState);
			int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(2);
			ItemStack dropStack = new ItemStack(harvestable.getItemDropped(cropState, event.world.rand, Short.MAX_VALUE), dropRate);

			if (enderLilly != null && harvestable == enderLilly && harvestableMeta >= 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
							new ItemStack(Items.ender_pearl));
					world.setBlockState(event.pos, harvestable.getDefaultState(), 2);
					world.spawnEntityInWorld(droppedItem);
					return true;
				}
			}
		}
		return false;
	}
}