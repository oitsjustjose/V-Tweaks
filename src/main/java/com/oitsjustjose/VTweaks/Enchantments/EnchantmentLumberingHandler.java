package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentLumberingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BreakEvent event)
	{
		if (event.state.getBlock() == null || event.world == null || event.getPlayer() == null || event.getPlayer().getHeldItem() == null)
			return;

		Block block = event.state.getBlock();
		World world = event.world;
		EntityPlayer player = event.getPlayer();
		BlockPos posUpOne = new BlockPos(event.pos.getX(), event.pos.getY() + 1, event.pos.getZ());

		// Checks
		boolean isTree = world.getBlockState(posUpOne).getBlock().isWood(world, posUpOne);

		// Checks to see if the tool is enchanted with lumbering
		if (EnchantmentHelper.getEnchantmentLevel(Config.lumberingEnchantmentID, player.getHeldItem()) > 0)
			// Checks if the block is a wood log, the player is sneaking, and the block above it is also a log (and therefore a tree)
			if (block.isWood(world, event.pos) && player.isSneaking() && isTree)
				// Starts loop for x coords
				for (int xPos = event.pos.getX() - 5; xPos <= event.pos.getX() + 5; xPos++)
					// Starts loop for y coords (should be tall enough for most trees)
					for (int yPos = event.pos.getY() - 1; yPos <= event.pos.getY() + 30; yPos++)
						// Starts loop for z coords
						for (int zPos = event.pos.getZ() - 5; zPos <= event.pos.getZ() + 5; zPos++)
						{
							BlockPos newPos = new BlockPos(xPos, yPos, zPos);
							// Checks if the block at coords is woood
							if (world.getBlockState(newPos).getBlock().isWood(world, newPos))
							{
								// Damages the tool for each block broken
								if (player.getHeldItem().attemptDamageItem(1, world.rand))
									return;
								// Breaks it
								breakBlock(world, player, newPos);
							}
						}
	}

	void breakBlock(World world, EntityPlayer player, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		IBlockState state = world.getBlockState(pos);

		block.onBlockHarvested(world, pos, state, player);
		if (block.removedByPlayer(world, pos, player, true))
		{
			block.onBlockDestroyedByPlayer(world, pos, state);
			block.harvestBlock(world, player, pos, state, world.getTileEntity(pos));
		}

		world.setBlockToAir(pos);
	}
}
