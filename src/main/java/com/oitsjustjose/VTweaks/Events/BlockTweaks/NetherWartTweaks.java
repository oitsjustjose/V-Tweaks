package com.oitsjustjose.VTweaks.Events.BlockTweaks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class NetherWartTweaks
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		// Drops out of the entire function if the block doesn't exist
		if (event.world.getBlock(event.x, event.y, event.z) == null)
			return;

		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		Block testFor = world.getBlock(event.x, event.y, event.z);
		Random rand = world.rand;

		if (event.action == event.action.RIGHT_CLICK_BLOCK && player.getCurrentEquippedItem() != null)
		{
			ItemStack heldItem = player.getCurrentEquippedItem();
			if (heldItem.getItem() == Items.blaze_powder)
			{
				// Check to see if the block is cactus or sugar cane
				if (testFor == Blocks.nether_wart && world.getBlockMetadata(event.x, event.y, event.z) < 3)
				{
					player.swingItem();
					for (int i = 0; i < 8; i++)
						spawnFX(world, rand, testFor, event);
					if (!world.isRemote)
					{
						if (!player.capabilities.isCreativeMode)
							--heldItem.stackSize;
						world.setBlockMetadataWithNotify(event.x, event.y, event.z, 3, 3);
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
		world.spawnParticle("happyVillager", (double) ((float) event.x + rand.nextFloat()),
				(double) event.y + (double) rand.nextFloat() * testFor.getBlockBoundsMaxY(),
				(double) ((float) event.z + rand.nextFloat()), d0, d1, d2);
	}
}