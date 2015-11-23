package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class EnchantmentLumberingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BreakEvent event)
	{
		if (event.block == null || event.world == null || event.getPlayer() == null || event.getPlayer().getHeldItem() == null)
			return;

		ItemStack block = new ItemStack(event.block, 1, event.blockMetadata);
		World world = event.world;
		EntityPlayer player = event.getPlayer();

		// Checks
		boolean isTree = world.getBlock(event.x, event.y + 1, event.z).isWood(world, event.x, event.y + 1, event.z);

		// Checks to see if the tool is enchanted with lumbering
		if (EnchantmentHelper.getEnchantmentLevel(Config.lumberingEnchantmentID, player.getHeldItem()) > 0)
			// Checks if the block is a wood log, the player is sneaking, and the block above it is also a log (and therefore a tree)
			if (event.block.isWood(world, event.x, event.y, event.z) && player.isSneaking() && isTree)
				// Starts loop for x coords
				for (int xPos = event.x - 5; xPos <= event.x + 5; xPos++)
					// Starts loop for y coords (should be tall enough for most trees)
					for (int yPos = event.y - 1; yPos <= event.y + 30; yPos++)
						// Starts loop for z coords
						for (int zPos = event.z - 5; zPos <= event.z + 5; zPos++)
							// Checks if the block at coords is woood
							if (world.getBlock(xPos, yPos, zPos).isWood(world, xPos, yPos, zPos))
							{
								//Damages the tool for each block broken
								if(player.getHeldItem().attemptDamageItem(1, world.rand))
									return;
								// Breaks it
								breakBlock(world, player, xPos, yPos, zPos);
							}
	}

	void breakBlock(World world, EntityPlayer player, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		block.onBlockHarvested(world, x, y, z, meta, player);
		if (block.removedByPlayer(world, player, x, y, z, true))
		{
			block.onBlockDestroyedByPlayer(world, x, y, z, meta);
			block.harvestBlock(world, player, x, y, z, meta);
		}

		world.setBlockToAir(x, y, z);
		if (!world.isRemote)
			world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
	}
}
