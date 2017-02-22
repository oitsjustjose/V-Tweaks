package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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
		// Checks if the feature is enabled
		if (!VTweaks.config.enableSleepingBags)
			return;

		// Checks if the held item is a Bed with the name "sleeping bag"
		if (event.getItemStack() == ItemStack.EMPTY || !(event.getItemStack().getItem() instanceof ItemBed) || !(event.getItemStack().getDisplayName().equalsIgnoreCase("sleeping bag")) || event.getEntityPlayer() == null)
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
		else if (world.isRemote)
			player.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
	}

	// A simple "tweak" packaged in with this one that prevents the named bed from being palced
	@SubscribeEvent
	public void registerTweak(PlayerInteractEvent event)
	{
		if (!VTweaks.config.enableSleepingBags)
			return;

		if (event.getItemStack() == ItemStack.EMPTY || !(event.getItemStack().getItem() instanceof ItemBed) || !(event.getItemStack().getDisplayName().equalsIgnoreCase("sleeping bag")) || event.getEntityPlayer() == null)
			return;
		// Assuming it's a bed item named "Sleeping Bag":
		if (event.isCancelable())
			event.setCanceled(true);
		event.setResult(Result.DENY);
		event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
	}

	void sleepSingleplayer(World world, EntityPlayer player, BlockPos pos)
	{
		final BlockPos origBedLoc = player.getBedLocation();
		final BlockPos origin = new BlockPos(0, 0, 0);

		world.setBlockState(origin, Blocks.BED.getDefaultState());

		SleepResult result = player.trySleep(origin);

		// This is actually GOING to happen, so may as well re-regulate
		if (result == SleepResult.TOO_FAR_AWAY)
			result = SleepResult.OK;

		// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
		if (result == SleepResult.OK)
		{
			player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(15), 40, 40, false, false));
			player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(2), 40, 3, false, false));
			player.bedLocation = pos;
			world.setWorldTime(0);
			player.wakeUpPlayer(false, true, false);
			world.setBlockToAir(origin);
			player.bedLocation = origBedLoc;
			player.sendStatusMessage(new TextComponentString(TextFormatting.GOLD + "Good Morning!"), true);
			player.swingArm(EnumHand.MAIN_HAND);
		}
		// Otherwise we warn them
		else
		{
			if (result == SleepResult.NOT_POSSIBLE_NOW)
				player.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
			else if (result == SleepResult.NOT_SAFE)
				player.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
		}
	}

	void sleepMultiplayer(World world, EntityPlayer player, BlockPos pos)
	{
		final BlockPos origBedLoc = player.getBedLocation();
		final BlockPos origin = new BlockPos(0, 0, 0);
		float sleeping = 0;

		// Counts how many online players are sleeping. Is there a better way? I like this well enough tho
		for (String s : player.getServer().getPlayerList().getOnlinePlayerNames())
			if (world.getPlayerEntityByName(s) != null && world.getPlayerEntityByName(s).isPlayerSleeping())
				sleeping++;

		// Requires that 50% of players are asleep before this works. If it does, the rest is very much like SSP
		if (sleeping / (float) player.getServer().getCurrentPlayerCount() >= 0.5)
		{
			player.getServer().getEntityWorld().setBlockState(origin, Blocks.BED.getDefaultState());

			SleepResult result = player.trySleep(origin);

			// This is actually GOING to happen, so may as well re-regulate
			if (result == SleepResult.TOO_FAR_AWAY)
				result = SleepResult.OK;

			// If the player CAN sleep, we set the time to 0 (default waking time) and wake up said player
			if (result == SleepResult.OK)
			{
				player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(15), 40, 40, false, false));
				player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObjectById(2), 40, 3, false, false));
				player.bedLocation = pos;
				world.setWorldTime(0);
				player.wakeUpPlayer(false, true, false);
				world.setBlockToAir(origin);
				player.bedLocation = origBedLoc;
				player.sendStatusMessage(new TextComponentString(TextFormatting.GOLD + "Good Morning!"), true);
				player.swingArm(EnumHand.MAIN_HAND);
			}
			// Otherwise we warn them
			else
			{
				if (result == SleepResult.NOT_POSSIBLE_NOW)
					player.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
				else if (result == SleepResult.NOT_SAFE)
					player.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
			}
		}
		// If not enough players are sleeping, they are notified. This translation is brought to you by V-Tweaks :)
		else if (!world.isRemote)
		{
			player.sendStatusMessage(new TextComponentTranslation("tile.bed.notEnoughSleep", new Object[0]), true);
		}
	}
}