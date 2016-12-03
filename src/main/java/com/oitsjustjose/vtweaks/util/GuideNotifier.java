package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuideNotifier
{
	@SubscribeEvent
	public void registerEvent(EntityJoinWorldEvent event)
	{
		final Entity entity = event.getEntity();
		final String SHOWN_LINK = "shownVTweaksLink";
		
		if(entity == null)
			return;
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			NBTTagCompound persistTag = getPlayerPersistTag(player, VTweaks.MODID);
			if(!persistTag.getBoolean(SHOWN_LINK) && !entity.world.isRemote)
			{
				Style style = new Style();
				String wikiURL = "http://oitsjustjose.github.io/V-Tweaks-Online/";
				style.setColor(TextFormatting.BLUE);
				style.setBold(true);
				style.setUnderlined(true);
				style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiURL));
				style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("V-Tweaks Wiki")));
				
				player.sendMessage(new TextComponentString("Welcome! Seems like this is your first time running V-Tweaks in this world! Here's a link to an online wiki if you're interested, keep in mind some features may be disabled!"));
				player.sendMessage(new TextComponentString("V-Tweaks Online Wiki").setStyle(style));
				
				persistTag.setBoolean(SHOWN_LINK, true);
			}
		}
	}
	
	public NBTTagCompound getPlayerPersistTag(EntityPlayer player, String modid)
	{
		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound persistTag = null;

		if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
		{
			persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}
		else
		{
			persistTag = new NBTTagCompound();
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
		}

		NBTTagCompound modTag = null;
		if (persistTag.hasKey(modid))
		{
			modTag = persistTag.getCompoundTag(modid);
		}
		else
		{
			modTag = new NBTTagCompound();
			persistTag.setTag(modid, modTag);
		}

		return modTag;
	}
}