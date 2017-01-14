package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropTweaks
{

	@SubscribeEvent
	@SuppressWarnings("deprecation")
	public void registerTweak(ItemExpireEvent event)
	{
		if (event.getEntityItem() == null || event.getEntityItem().getEntityItem() == null)
			return;

		EntityItem entItem = event.getEntityItem();
		World world = entItem.getEntityWorld();
		ItemStack stack = entItem.getEntityItem();

		// Handles egg hatching; 1 in 12 chance.
		if (VTweaks.config.enableDropTweaksEggHatching && stack.getItem() == Items.EGG)
		{
			if (world.rand.nextInt(VTweaks.config.enableEggHatchChance) == 0)
			{
				if (!world.isRemote)
				{
					EntityChicken chick = new EntityChicken(world);
					chick.setGrowingAge(-24000);
					chick.setLocationAndAngles(entItem.posX, entItem.posY, entItem.posZ, entItem.rotationYaw, 0.0F);
					world.spawnEntityInWorld(chick);
				}
			}
		}
		// Handles sapling replanting; 100% chance
		else if (VTweaks.config.enableDropTweaksSaplings && Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockSapling)
		{
			BlockPos saplingPos = new BlockPos((int) entItem.posX, (int) entItem.posY, (int) entItem.posZ);
			// Checks to see if where the sapling *will* be is air
			if (world.isAirBlock(saplingPos))
			{
				// Checks to see if the block below it is ground
				Material blockMat = world.getBlockState(saplingPos.down()).getMaterial();
				if (blockMat == Material.GRASS || blockMat == Material.GROUND)
				{
					IBlockState state = Block.getBlockFromItem(stack.getItem()).getStateFromMeta(stack.getItemDamage());
					world.setBlockState(saplingPos, state);
				}
			}
		}
		// Only gets run if the config is set to make all items never despawn.
		else if (VTweaks.config.enableDropTweaksDespawn && VTweaks.config.enableNewDespawnTime == -1)
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void registerTweak(ItemTossEvent event)
	{
		// Checks to see if the despawn time is -1. If it is, items won't despawn, so nothing to do here.
		if (!VTweaks.config.enableDropTweaksDespawn || VTweaks.config.enableNewDespawnTime == -1 || event.getEntityItem() == null)
			return;

		EntityItem entItem = event.getEntityItem();
		entItem.lifespan = VTweaks.config.enableNewDespawnTime;
	}
}