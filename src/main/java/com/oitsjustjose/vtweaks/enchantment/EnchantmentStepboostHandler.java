package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentStepboostHandler
{
	@SubscribeEvent
	public void register(PlayerEvent event)
	{
		ItemStack boots = event.getEntityPlayer().inventory.armorInventory[0];
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Enchantments.stepboost, boots);

		if (boots != null && EnchantmentLevelArmor != 0)
			event.getEntityPlayer().stepHeight = 1.0F;
		else
			event.getEntityPlayer().stepHeight = 0.5F;
	}
}