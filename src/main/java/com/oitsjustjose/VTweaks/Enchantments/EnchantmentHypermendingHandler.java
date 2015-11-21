package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentHypermendingHandler
{
	/*
	 * This part of the code does the actual work for the enchantment Without this class, the enchantment exists, but
	 * does not actually do a single thing....
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(PlayerEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = null;
		ItemStack armor = null;
		int EnchantmentLevelTool;
		int EnchantmentLevelArmor;
		boolean isValid = isValidTool(player.getCurrentEquippedItem());

		// Handles Item Reparation
		if (player.getCurrentEquippedItem() != null && isValid)
		{
			heldItem = player.getCurrentEquippedItem();
			if (heldItem != null && heldItem.getItemDamage() > 0)
			{
				EnchantmentLevelTool = EnchantmentHelper.getEnchantmentLevel(Config.hypermendingEnchantmentID,
						heldItem);
				if (EnchantmentLevelTool > 0)
					heldItem.setItemDamage(0);
			}
		}

		// Handles Armor Reparation
		for (int i = 0; i < 4; i++)
			if (player.getCurrentArmor(i) != null)
			{
				armor = player.getCurrentArmor(i);
				EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Config.hypermendingEnchantmentID, armor);
				if (EnchantmentLevelArmor > 0)
					armor.setItemDamage(0);
			}
	}

	// This code determines if the held item exists, and is either ItemTool or
	// ItemSword (to prevent
	// weird stuff from happening if the enchantment somehow ends up on a
	// non-tool item like a potato..
	boolean isValidTool(ItemStack itemstack)
	{
		if (itemstack != null)
		{
			if (itemstack.getItem() instanceof ItemTool)
				return true;
			if (itemstack.getItem() instanceof ItemSword)
				return true;
			if (itemstack.getItem() instanceof ItemBow)
				return true;
			if (itemstack.getItem() instanceof ItemHoe)
				return true;
		}
		return false;
	}
}