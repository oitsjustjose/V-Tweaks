package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SleepingBags
{
	@SubscribeEvent
	public void registerTweak(RightClickItem event)
	{
		if (!VTweaks.config.enableSleepingBags)
			return;

		if (event.getItemStack() == null || !isSleepingBag(event.getItemStack()) || event.getEntityPlayer() == null)
			return;

		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();

		// It's gotta be night time.. hurdur
		if (!world.provider.isDaytime())
		{
			// Checking if it's SMP; that's important
			if (player.getServer().isDedicatedServer())
				sleepMultiplayer(world, player, event.getPos());
			// SSP is a little bit easier.. only somewhat
			else
				sleepSingleplayer(world, player, event.getPos());
		}
		// And finally - if it's not even night time - they should... know?
		else if (!world.isRemote)
			player.addChatComponentMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]));

	}

	// A simple "tweak" packaged in with this one that prevents the named bed from being palced
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (!VTweaks.config.enableSleepingBags)
			return;

		if (event.getItemStack() == null || !isSleepingBag(event.getItemStack()) || event.getEntityPlayer() == null)
			return;
		// Assuming it's a bed item named "Sleeping Bag":
		if (event.isCancelable())
			event.setCanceled(true);
		event.setResult(Result.DENY);
		event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
	}

	private void sleepSingleplayer(World world, EntityPlayer player, BlockPos pos)
	{
		SleepResult result = player.trySleep(pos);

		// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
		if (result == SleepResult.OK)
		{
			world.provider.setWorldTime((world.getWorldTime() / 24000) * 24000 + 24000);
			world.provider.resetRainAndThunder();
			player.wakeUpPlayer(false, true, false);
			player.swingArm(EnumHand.MAIN_HAND);
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

	private void sleepMultiplayer(World world, EntityPlayer player, BlockPos pos)
	{
		float sleeping = 1;

		// Counts how many online players are sleeping. Is there a better way? I like this well enough tho
		for (String s : player.getServer().getAllUsernames())
			if (world.getPlayerEntityByName(s) != null && world.getPlayerEntityByName(s).isPlayerSleeping())
				sleeping++;

		// Requires that 50% of players are asleep before this works. If it does, the rest is very much like SSP
		if (sleeping / (float) player.getServer().getCurrentPlayerCount() >= 0.5)
		{
			SleepResult result = player.trySleep(pos);

			// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
			if (result == SleepResult.OK)
			{
				world.provider.setWorldTime((world.getWorldTime() / 24000) * 24000 + 24000);
				world.provider.resetRainAndThunder();
				player.wakeUpPlayer(false, true, false);
				player.swingArm(EnumHand.MAIN_HAND);
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

	// Ensures Quark compatibility!
	private boolean isSleepingBag(ItemStack itemstack)
	{
		return (itemstack.getItem().getClass().getName().contains("Bed")) && itemstack.getDisplayName().equalsIgnoreCase("sleeping bag");
	}
}