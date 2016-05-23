package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentHypermendingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(PlayerEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		ItemStack heldItem = null;
		ItemStack armor = null;
		int EnchantmentLevelTool;
		int EnchantmentLevelArmor;
		boolean isValid = isValidTool(player.getCurrentEquippedItem());

		if (player.getCurrentEquippedItem() != null && isValid)
		{
			heldItem = player.getCurrentEquippedItem();
			if (heldItem != null && heldItem.getItemDamage() > 0)
			{
				EnchantmentLevelTool = EnchantmentHelper.getEnchantmentLevel(VTweaks.modConfig.hypermendingID, heldItem);
				if (EnchantmentLevelTool > 0)
					heldItem.setItemDamage(0);
			}
		}

		for (int i = 0; i < 4; i++)
		{
			if (player.getCurrentArmor(i) != null)
			{
				armor = player.getCurrentArmor(i);
				EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(VTweaks.modConfig.hypermendingID, armor);
				if (EnchantmentLevelArmor > 0)
					armor.setItemDamage(0);
			}
		}
	}

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