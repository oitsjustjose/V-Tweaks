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
		if(VTweaks.config.hypermendingID <= 0)
			return;
		
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();

		if (heldItem != null && isValidTool(player.getHeldItemMainhand()))
			if (heldItem != null && heldItem.getItemDamage() > 0)
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.hyperMending, heldItem) > 0)
					heldItem.setItemDamage(0);

		if (player.getArmorInventoryList() != null)
			for (ItemStack stack : player.getArmorInventoryList())
				if (stack != null)
					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.hyperMending, stack) > 0)
						stack.setItemDamage(0);
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