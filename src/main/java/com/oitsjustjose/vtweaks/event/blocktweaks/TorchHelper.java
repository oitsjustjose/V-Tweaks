package com.oitsjustjose.vtweaks.event.blocktweaks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TorchHelper
{
	@SubscribeEvent
	public void registerTweak(RightClickBlock event)
	{
		if (event.getEntityPlayer().getHeldItemMainhand() == null || event.getEntityPlayer().getHeldItemOffhand() != null)
			return;

		EntityPlayer player = event.getEntityPlayer();
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block block = state.getBlock();

		if (!(player.getHeldItemMainhand().getItem() instanceof ItemTool) || block.hasTileEntity(state))
			return;

		if (!event.getWorld().isRemote && block.onBlockActivated(event.getWorld(), event.getPos(), state, player, EnumHand.MAIN_HAND, player.getHeldItemMainhand(), event.getFace(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()))
			event.setCanceled(true);
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && stack.getDisplayName().toLowerCase().matches(Blocks.TORCH.getLocalizedName().toLowerCase()))
				{
					if (stack.onItemUse(player, event.getWorld(), event.getPos(), EnumHand.MAIN_HAND, event.getFace(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()) == EnumActionResult.SUCCESS)
						player.swingArm(EnumHand.MAIN_HAND);
					if (stack.stackSize == 0)
						player.inventory.setInventorySlotContents(i, null);
				}
			}
		}

	}
}
