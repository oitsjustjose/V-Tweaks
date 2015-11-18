package com.oitsjustjose.VTweaks.Events.BlockTweaks;

import net.minecraft.block.Block;
//import com.pam.harvestcraft.BlockPamCrop;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class CropHelperModSupport
{
	public static void registerWitchery(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("witchery"))
		{
			World world = event.world;
			Block harvestable = world.getBlock(event.x, event.y, event.z);
			int harvestableMeta = world.getBlockMetadata(event.x, event.y, event.z);
			int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(2);
			ItemStack dropStack = new ItemStack(
					harvestable.getItemDropped(harvestableMeta, event.world.rand, Short.MAX_VALUE), dropRate);

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
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, dropStack);
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (artichokeCrop != null && harvestable == artichokeCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(ingredient, dropRate, artichoke));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (wolfsbaneCrop != null && harvestable == wolfsbaneCrop && harvestableMeta == 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(ingredient, dropRate, wolfsbane));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (belladonnaCrop != null && harvestable == belladonnaCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(ingredient, dropRate, belladonna));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}

			if (snowbellCrop != null && harvestable == snowbellCrop && harvestableMeta == 4)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem1 = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, dropStack);
					EntityItem droppedItem2 = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(ingredient, 1, icyNeedle));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
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
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(ingredient, dropRate, wormwood));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}
			}
		}
	}

	public static void registerNatura(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("Natura"))
		{
			World world = event.world;
			Block harvestable = world.getBlock(event.x, event.y, event.z);
			int harvestableMeta = world.getBlockMetadata(event.x, event.y, event.z);
			int dropRate = harvestable.quantityDropped(world.rand) + world.rand.nextInt(2);
			ItemStack dropStack = new ItemStack(
					harvestable.getItemDropped(harvestableMeta, event.world.rand, Short.MAX_VALUE), dropRate);

			Block barley = GameRegistry.findBlock("Natura", "N Crops");
			if (barley == null)
				return;
			if (harvestable == barley && harvestableMeta == 3)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, dropStack);
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
				}

			}
		}
	}

	public static boolean registerExU(PlayerInteractEvent event)
	{
		if (Loader.isModLoaded("ExtraUtilities"))
		{
			World world = event.world;
			Block harvestable = world.getBlock(event.x, event.y, event.z);
			int harvestableMeta = world.getBlockMetadata(event.x, event.y, event.z);
			Block enderLilly = GameRegistry.findBlock("ExtraUtilities", "plant/ender_lilly");
			if (enderLilly != null && harvestable == enderLilly && harvestableMeta >= 7)
			{
				event.entityPlayer.swingItem();
				if (!world.isRemote)
				{
					EntityItem droppedItem = new EntityItem(world, event.entityPlayer.posX, event.entityPlayer.posY,
							event.entityPlayer.posZ, new ItemStack(Items.ender_pearl));
					world.setBlockMetadataWithNotify(event.x, event.y, event.z, 0, 3);
					world.spawnEntityInWorld(droppedItem);
					return true;
				}
			}
		}
		return false;
	}
}