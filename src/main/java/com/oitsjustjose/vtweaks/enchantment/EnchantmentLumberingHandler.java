package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentLumberingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BreakEvent event)
	{
		if (event.getState().getBlock() == null || event.getWorld() == null || event.getPlayer() == null || event.getPlayer().getHeldItemMainhand() == null)
			return;

		Block block = event.getState().getBlock();
		World world = event.getWorld();
		EntityPlayer player = event.getPlayer();
		boolean isTree = world.getBlockState(event.getPos().up()).getBlock().isWood(world, event.getPos().up());

		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.lumbering, player.getHeldItemMainhand()) > 0)
			if (block.isWood(world, event.getPos()) && player.isSneaking() && isTree)
				for (int xPos = event.getPos().getX() - 5; xPos <= event.getPos().getX() + 5; xPos++)
					for (int yPos = event.getPos().getY() - 1; yPos <= event.getPos().getY() + 30; yPos++)
						for (int zPos = event.getPos().getZ() - 5; zPos <= event.getPos().getZ() + 5; zPos++)
						{
							BlockPos newPos = new BlockPos(xPos, yPos, zPos);
							if (world.getBlockState(newPos).getBlock().isWood(world, newPos))
							{
								if (player.getHeldItemMainhand().attemptDamageItem(1, world.rand))
									return;
								breakBlock(world, player, newPos);
							}
						}
	}

	void breakBlock(World world, EntityPlayer player, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		IBlockState state = world.getBlockState(pos);

		block.onBlockHarvested(world, pos, state, player);
		if (block.removedByPlayer(state, world, pos, player, true))
		{
			block.onBlockDestroyedByPlayer(world, pos, state);
			block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
		}

		world.setBlockToAir(pos);
	}
}
