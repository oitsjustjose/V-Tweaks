package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentStepboostHandler
{
	@SubscribeEvent
	public void register(PlayerEvent event)
	{
		ItemStack boots = event.entityPlayer.getCurrentArmor(0);
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Config.stepboostEnchantmentID, boots);

		if (boots != null && EnchantmentLevelArmor != 0)
			event.entityPlayer.stepHeight = 1.0F;
		else
			event.entityPlayer.stepHeight = 0.5F;
	}
}