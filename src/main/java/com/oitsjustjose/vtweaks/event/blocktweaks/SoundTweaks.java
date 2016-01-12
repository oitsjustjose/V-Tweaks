package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.ArrayList;
import java.util.List;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundTweaks
{
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (event.action != event.action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;

		// This is a variable created for.. caching? Yeah. Caching
		ArrayList<ItemStack> cachedItem = new ArrayList<ItemStack>();
		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		ItemStack heldItem = player.getHeldItem();
		BlockPos pos = event.pos;

		if (heldItem != null && heldItem.getItem() instanceof ItemDoor)
		{
			cachedItem.add(heldItem);
			IBlockState blockState = world.getBlockState(event.pos);
			Block block = blockState.getBlock();

			ItemDoor door = (ItemDoor) heldItem.getItem();
			if (door.onItemUse(heldItem, player, world, event.pos, event.face, event.pos.getX(), event.pos.getY(), event.pos.getZ()))
			{
				if (heldItem.getItem() == Items.iron_door)
					world.playSoundEffect(event.pos.getX(), event.pos.getY(), event.pos.getZ(), Block.soundTypeMetal.getPlaceSound(), 1.0F, 1.0F);
				else
					world.playSoundEffect(event.pos.getX(), event.pos.getY(), event.pos.getZ(), Block.soundTypeWood.getPlaceSound(), 1.0F, 0.75F);

				if (player.capabilities.isCreativeMode)
				{
					player.inventory.addItemStackToInventory(new ItemStack(cachedItem.get(0).getItem(), 1, cachedItem.get(0).getMetadata()));
					cachedItem.clear();
				}
				player.swingItem();
			}
		}
	}
}
