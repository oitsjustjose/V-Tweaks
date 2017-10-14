package com.oitsjustjose.vtweaks.enchantment.handler;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.Config;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentHypermendingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(PlayerEvent event)
	{
		// Check if enchantment is disabled
		if (!Config.getInstance().enableEnchHypermending || event.getEntityPlayer() == null)
			return;
		// Local variables
		EntityPlayer player = event.getEntityPlayer();
		ArrayList<ItemStack> fullInventory = new ArrayList<ItemStack>();
		// Merges all inventory slots into one list
		fullInventory.addAll(player.inventory.mainInventory);
		fullInventory.addAll(player.inventory.offHandInventory);
		fullInventory.addAll(player.inventory.armorInventory);
		// Keeps entire inventory of tools constantly repaired
		for (ItemStack stack : fullInventory)
			if (!stack.isEmpty() && stack.isItemStackDamageable() && stack.isItemDamaged() && EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().hypermending, stack) > 0)
				stack.setItemDamage(0);
	}
}