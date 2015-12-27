package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TorchHelper
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.entityPlayer.getCurrentEquippedItem() == null)
			return;

		EntityPlayer player = event.entityPlayer;
		IBlockState state = event.world.getBlockState(event.pos);
		Boolean hasGUI = state.getBlock().hasTileEntity(state) || state.getBlock() instanceof BlockWorkbench;

		if (!(player.getHeldItem().getItem() instanceof ItemTool) || hasGUI)
			return;

		if (event.action == event.action.RIGHT_CLICK_BLOCK)
		{
			if (player.inventory.hasItem(Item.getItemFromBlock(Blocks.torch)))
			{
				ItemStack torch = new ItemStack(Blocks.torch);
				if (torch.onItemUse(player, event.world, event.pos, event.face, event.pos.getX(), event.pos.getY(), event.pos.getZ()))
				{
					player.swingItem();
					if (!player.capabilities.isCreativeMode)
						player.inventory.consumeInventoryItem(Item.getItemFromBlock(Blocks.torch));
				}
			}
		}
	}
}
