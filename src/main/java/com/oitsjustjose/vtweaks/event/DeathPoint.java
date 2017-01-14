package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DeathPoint
{
	@SubscribeEvent
	public void registerTweak(LivingDeathEvent event)
	{
		if(!VTweaks.config.enableDeathPoint)
			return;
		
		if (!(event.getEntity() instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) event.getEntity();
		player.addChatMessage(getCoordMessage(event.getEntity().getPosition()));
	}

	TextComponentString getCoordMessage(BlockPos pos)
	{
		String message = "Your most recent death was at: " + TextFormatting.YELLOW + "[X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ() + "]";
		return new TextComponentString(message);
	}
}