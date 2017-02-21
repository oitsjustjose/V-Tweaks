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

		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();

		// It's gotta be night time.. hurdur
		if (!world.isDaytime())
		{
			// Checking if it's SMP; that's important
			if (player.getServer().isDedicatedServer())
			{
				float sleeping = 0;

				// Counts how many online players are sleeping. Is there a better way? I like this well enough tho
				for (String s : player.getServer().getAllUsernames())
					if (world.getPlayerEntityByName(s) != null && world.getPlayerEntityByName(s).isPlayerSleeping())
						sleeping++;

				// Requires that 50% of players are asleep before this works. If it does, the rest is very much like SSP
				if (sleeping / (float) player.getServer().getCurrentPlayerCount() >= 0.5)
				{
					SleepResult result = player.trySleep(event.getPos());

					// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
					if (result == SleepResult.OK)
					{
						world.setWorldTime(0);
						player.wakeUpPlayer(false, true, false);
					}
					// Otherwise we warn them
					else
					{
						if (result == SleepResult.NOT_POSSIBLE_NOW)
							player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));
						else if (result == SleepResult.NOT_SAFE)
							player.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]));
					}
				}
				// If not enough players are sleeping, they are notified. This translation is brought to you by V-Tweaks :)
				else if (!world.isRemote)
				{
					player.addChatComponentMessage(new TextComponentTranslation("tile.bed.notEnoughSleep", new Object[0]));
				}
			}
			// SSP is a little bit easier.. only somewhat
			else
			{
				SleepResult result = player.trySleep(event.getPos());

				// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
				if (result == SleepResult.OK)
				{
					world.setWorldTime(0);
					player.wakeUpPlayer(false, true, false);
				}
				// Otherwise we warn them
				else
				{
					if (result == SleepResult.NOT_POSSIBLE_NOW)
						player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));
					else if (result == SleepResult.NOT_SAFE)
						player.addChatComponentMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]));
				}
			}
		}
		// And finally - if it's not even night time - they should... know?
		else if (!world.isRemote)
			player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));

	}

	// A simple "tweak" packaged in with this one that prevents the named bed from being palced
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (!VTweaks.config.enablePocketBed)
			return;

		if (event.getItemStack() == null || !(event.getItemStack().getItem() instanceof ItemBed) || !(event.getItemStack().getDisplayName().equalsIgnoreCase("sleeping bag")) || event.getEntityPlayer() == null)
			return;
		// Assuming it's a bed item named "Sleeping Bag":
		if (event.isCancelable())
			event.setCanceled(true);
		event.setResult(Result.DENY);
	}
}