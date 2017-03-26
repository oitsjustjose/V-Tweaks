package com.oitsjustjose.vtweaks.event.itemtweaks;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DropTweaks
{

	@SubscribeEvent
	public void registerTweak(ItemExpireEvent event)
	{
		if (event.getEntityItem() == null || event.getEntityItem().getEntityItem() == null)
			return;

		EntityItem entItem = event.getEntityItem();
		World world = entItem.getEntityWorld();
		ItemStack stack = entItem.getEntityItem();

		// Handles egg hatching; configurable chance.
		if (VTweaks.config.enableDropTweaksEggHatching && stack.getItem() == Items.EGG)
		{
			if (world.rand.nextInt(VTweaks.config.dropTweaksEggHatchingChance) == 0)
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
		else if (VTweaks.config.enableDropTweaksSaplings && Block.getBlockFromItem(stack.getItem()) != null && Block.getBlockFromItem(stack.getItem()).getClass().getName().contains("BlockSapling"))
		{
			BlockPos saplingPos = fromDouble(entItem.posX, entItem.posY, entItem.posZ);
			// Checks to see if where the sapling *will* be is air
			if (world.isAirBlock(saplingPos))
				stack.onItemUse(new FakePlayer(world.getMinecraftServer().worldServerForDimension(world.provider.getDimension()), new GameProfile(UUID.nameUUIDFromBytes(VTweaks.MODID.getBytes()), "VTweaksFake")), world, saplingPos, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);

		}
		// Only gets run if the config is set to make all items never despawn.
		else if (VTweaks.config.enableDropTweaksDespawn && VTweaks.config.dropTweaksNewDespawnTime == -1)
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void registerTweak(ItemTossEvent event)
	{
		// Checks to see if the despawn time is -1. If it is, items won't despawn, so nothing to do here.
		if (!VTweaks.config.enableDropTweaksDespawn || VTweaks.config.dropTweaksNewDespawnTime == -1 || event.getEntityItem() == null)
			return;

		EntityItem entItem = event.getEntityItem();
		entItem.lifespan = VTweaks.config.dropTweaksNewDespawnTime;
	}

	private BlockPos fromDouble(double xIn, double yIn, double zIn)
	{
		int x = (xIn % 1) >= .5 ? (int) Math.ceil(xIn) : (int) Math.floor(xIn);
		int y = (yIn % 1) >= .5 ? (int) Math.ceil(yIn) : (int) Math.floor(yIn);
		int z = (zIn % 1) >= .5 ? (int) Math.ceil(zIn) : (int) Math.floor(zIn);
		return new BlockPos(x, y, z);
	}

}