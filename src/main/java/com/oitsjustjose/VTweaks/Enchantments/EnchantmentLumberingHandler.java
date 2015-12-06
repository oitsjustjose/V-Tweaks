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
		boolean isTree = world.getBlockState(event.pos.up()).getBlock().isWood(world, event.pos.up());

		if (EnchantmentHelper.getEnchantmentLevel(Config.lumberingEnchantmentID, player.getHeldItem()) > 0)
			if (block.isWood(world, event.pos) && player.isSneaking() && isTree)
				for (int xPos = event.pos.getX() - 5; xPos <= event.pos.getX() + 5; xPos++)
					for (int yPos = event.pos.getY() - 1; yPos <= event.pos.getY() + 30; yPos++)
						for (int zPos = event.pos.getZ() - 5; zPos <= event.pos.getZ() + 5; zPos++)
						{
							BlockPos newPos = new BlockPos(xPos, yPos, zPos);
							if (world.getBlockState(newPos).getBlock().isWood(world, newPos))
							{
								if (player.getHeldItem().attemptDamageItem(1, world.rand))
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
		if (block.removedByPlayer(world, pos, player, true))
		{
			block.onBlockDestroyedByPlayer(world, pos, state);
			block.harvestBlock(world, player, pos, state, world.getTileEntity(pos));
		}

		world.setBlockToAir(pos);
	}
}
