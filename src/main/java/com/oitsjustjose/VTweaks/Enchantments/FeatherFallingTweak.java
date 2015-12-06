package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeatherFallingTweak
{
	@SubscribeEvent
	public void registerTweak(PlayerEvent event)
	{
		if (event.entityPlayer.getCurrentArmor(0) == null)
			return;
		ItemStack boots = event.entityPlayer.getCurrentArmor(0);
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, boots);
		if (boots != null && EnchantmentLevelArmor >= 4)
			event.entityPlayer.fallDistance = 0.0F;
	}
}