package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TorchHelper
{
	@SubscribeEvent
	public void registerTweak(RightClickBlock event)
	{
		if (!VTweaks.config.enableTorchHelper)
			return;

		if (event.getEntityPlayer().getHeldItemMainhand().isEmpty() || !event.getEntityPlayer().getHeldItemOffhand().isEmpty())
			return;

		EntityPlayer player = event.getEntityPlayer();
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block block = state.getBlock();

		if (!(player.getHeldItemMainhand().getItem() instanceof ItemTool) || block.hasTileEntity(state))
			return;

		if (!event.getWorld().isRemote && block.onBlockActivated(event.getWorld(), event.getPos(), state, player, EnumHand.MAIN_HAND, event.getFace(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()))
			event.setCanceled(true);
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			{
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (!stack.isEmpty() && stack.getItem() == Item.getItemFromBlock(Blocks.TORCH))
				{
					if (placeTorch(event.getWorld(), player, event.getPos(), event.getFace(), stack) == EnumActionResult.SUCCESS)
						player.swingArm(EnumHand.MAIN_HAND);
					break;
				}
			}
		}
	}

	public EnumActionResult placeTorch(World world, EntityPlayer player, BlockPos pos, EnumFacing face, ItemStack itemstack)
	{
		BlockPos posToCheck = pos;

		switch (face)
		{
		case UP:
			posToCheck = pos.up();
			break;
		case DOWN:
			posToCheck = pos.down();
			break;
		case NORTH:
			posToCheck = pos.north();
			break;
		case EAST:
			posToCheck = pos.east();
			break;
		case SOUTH:
			posToCheck = pos.south();
			break;
		case WEST:
			posToCheck = pos.west();
			break;
		}

		IBlockState state = world.getBlockState(posToCheck);
		IBlockState torch = Blocks.TORCH.getStateForPlacement(world, posToCheck, face, posToCheck.getX(), posToCheck.getY(), posToCheck.getZ(), 0, player, EnumHand.MAIN_HAND);

		if (state.getBlock().isAir(state, world, posToCheck) || state.getBlock().isReplaceable(world, posToCheck))
		{
			if (Blocks.TORCH.canPlaceBlockAt(world, posToCheck))
			{
				SoundType torchSound = Blocks.TORCH.getSoundType(torch, world, posToCheck, player);
				world.playSound(player, posToCheck, torchSound.getPlaceSound(), SoundCategory.BLOCKS, torchSound.getVolume(), 0.8F);
				world.setBlockState(posToCheck, torch);
				itemstack.setCount(itemstack.getCount() - 1);
				return EnumActionResult.SUCCESS;
			}
		}

		return EnumActionResult.FAIL;
	}
}
