package com.oitsjustjose.vtweaks.enchantment.handler;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.actors.threadpool.Arrays;

public class EnchantmentHypermendingHandler
{
	@SuppressWarnings("unchecked")
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(PlayerEvent event)
	{
		// Check if enchantment is disabled
		if (VTweaks.config.hypermendingID <= 0)
			return;
		// Local variables
		EntityPlayer player = event.getEntityPlayer();
		ArrayList<ItemStack> fullInventory = new ArrayList<ItemStack>();
		// Merges all inventory slots into one list
		fullInventory.addAll(Arrays.asList(player.inventory.mainInventory));
		fullInventory.addAll(Arrays.asList(player.inventory.offHandInventory));
		fullInventory.addAll(Arrays.asList(player.inventory.armorInventory));
		// Keeps entire inventory of tools constantly repaired
		for (ItemStack stack : fullInventory)
			if (stack != null && stack.isItemStackDamageable() && stack.isItemDamaged() && EnchantmentHelper.getEnchantmentLevel(Enchantments.hypermending, stack) > 0)
				stack.setItemDamage(0);
	}
}