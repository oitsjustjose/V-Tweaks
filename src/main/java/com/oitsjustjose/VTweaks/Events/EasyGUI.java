package com.oitsjustjose.VTweaks.Events;

import com.oitsjustjose.VTweaks.Util.GuideBook;
import com.oitsjustjose.VTweaks.Util.KeyBindings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EasyGUI
{
	@SubscribeEvent
	public void registerTweak(PlayerEvent event)
	{
		if (event.entityPlayer == null)
			return;
		EntityPlayer player = event.entityPlayer;
		if (KeyBindings.extraInventory.isPressed())
		{
			InventoryEnderChest enderChest = player.getInventoryEnderChest();
			enderChest.setCustomName(EnumChatFormatting.DARK_PURPLE + "Ender Items");
			player.displayGUIChest(enderChest);
		}
		else if (KeyBindings.crafting.isPressed())
		{
			player.displayGUIBook(GuideBook.getGuideBook());
		}
	}
}
