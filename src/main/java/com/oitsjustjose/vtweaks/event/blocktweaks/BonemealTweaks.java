package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BonemealTweaks
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;

		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		Block testFor = world.getBlockState(event.pos).getBlock();
		Random rand = world.rand;

		if (event.action == event.action.RIGHT_CLICK_BLOCK && player.getCurrentEquippedItem() != null)
		{
			ItemStack heldItem = player.getCurrentEquippedItem();
			if (heldItem.getItemDamage() == 15 && heldItem.getItem() == Items.dye)
			{
				if (testFor == Blocks.cactus || testFor == Blocks.reeds)
				{
					if (world.getBlockState(event.pos.down(2)).getBlock() != testFor && world.isAirBlock(event.pos.up()))
					{
						player.swingItem();
						for (int i = 0; i < 8; i++)
							spawnFX(world, rand, testFor, event);
						if (!world.isRemote)
						{
							if (!player.capabilities.isCreativeMode)
								--heldItem.stackSize;
							world.setBlockState(event.pos.up(), world.getBlockState(event.pos), 2);
						}
					}
				}
			}
		}
	}

	void spawnFX(World world, Random rand, Block testFor, PlayerInteractEvent event)
	{
		double d0 = rand.nextGaussian() * 0.02D;
		double d1 = rand.nextGaussian() * 0.02D;
		double d2 = rand.nextGaussian() * 0.02D;
		world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double) ((float) event.pos.getX() + rand.nextFloat()), (double) event.pos.getY() + (double) rand.nextFloat() * testFor
				.getBlockBoundsMaxY(), (double) ((float) event.pos.getZ() + rand.nextFloat()), d0, d1, d2);
	}
}