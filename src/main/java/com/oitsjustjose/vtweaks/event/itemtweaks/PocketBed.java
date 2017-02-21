package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.ItemBed;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PocketBed
{
	@SubscribeEvent
	public void registerTweak(RightClickItem event)
	{
		if (!VTweaks.config.enablePocketBed)
			return;

		if (event.getItemStack() == null || !(event.getItemStack().getItem() instanceof ItemBed) || !(event.getItemStack().getDisplayName().equalsIgnoreCase("sleeping bag")) || event.getEntityPlayer() == null)
			return;
		
		event.setResult(Result.DENY);

		
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();

		if (player.isCreative())
			return;

		if (!world.isDaytime())
		{
			SleepResult result = player.trySleep(event.getPos());

			if (result == SleepResult.OK)
			{
				world.setWorldTime(0);
				player.wakeUpPlayer(false, true, false);
			}
			else
			{
				if (result == SleepResult.NOT_POSSIBLE_NOW)
					player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));
				else if (result == SleepResult.NOT_SAFE)
					player.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]));
			}
		}
		else if (!world.isRemote)
			player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));
	}
	
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (!VTweaks.config.enablePocketBed)
			return;

		if (event.getItemStack() == null || !(event.getItemStack().getItem() instanceof ItemBed) || !(event.getItemStack().getDisplayName().equalsIgnoreCase("sleeping bag")) || event.getEntityPlayer() == null)
			return;
		
		event.setCanceled(true);
		event.setResult(Result.DENY);
	}
}