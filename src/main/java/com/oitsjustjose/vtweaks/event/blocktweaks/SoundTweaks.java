package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundTweaks
{

	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event) throws IllegalArgumentException, IllegalAccessException
	{
		if (event.action != Action.RIGHT_CLICK_BLOCK || event.world.getBlockState(event.pos) == null)
			return;

		// Creative Mode Item Cache
		ArrayList<ItemStack> cache = new ArrayList<ItemStack>();
		EntityPlayer player = event.entityPlayer;
		World world = event.world;
		ItemStack heldItem = player.getHeldItem();

		if (heldItem != null && heldItem.getItem() instanceof ItemDoor)
		{
			cache.add(heldItem);
			ItemDoor door = (ItemDoor) heldItem.getItem();
			Block block = (Block) ReflectionHelper.findField(ItemDoor.class, "block").get(door);

			if (door.onItemUse(heldItem, player, world, event.pos, event.face, event.pos.getX(), event.pos.getY(), event.pos.getZ()))
			{
				if (player.capabilities.isCreativeMode)
				{
					player.inventory.addItemStackToInventory(cache.get(0).copy());
					cache.get(0).stackSize = 1;
					cache.clear();
				}
				world.playSoundEffect(event.pos.getX(), event.pos.getY(), event.pos.getZ(), block.stepSound.getPlaceSound(), 1.0F, block.stepSound.getFrequency() - 0.25F);
				player.swingItem();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static class VillagerTweak
	{
		static final VillagerTweak instance = new VillagerTweak();

		public static VillagerTweak getInstance()
		{
			return instance;
		}

		@SubscribeEvent
		public void registerTweak(SoundSourceEvent event)
		{
			if (event.name != null)
				if (event.name.contains("mob.villager"))
					event.setCanceled(true);
		}
	}
}
