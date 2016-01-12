package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SignEditor
{
	@SubscribeEvent
	public void registerEvent(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;
		if (event.entityPlayer.getHeldItem() != null)
			return;

		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		Block block = world.getBlockState(event.pos).getBlock();

		if (block instanceof BlockSign && !world.isRemote && player.isSneaking())
		{
			TileEntitySign sign = (TileEntitySign) world.getTileEntity(event.pos);
			if (sign != null)
				player.openEditSign(sign);
		}
	}
}
