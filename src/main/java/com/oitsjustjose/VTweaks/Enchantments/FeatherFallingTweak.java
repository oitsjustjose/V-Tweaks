package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FeatherFallingTweak
{
	/*
	 * Better Feather Falling is intended for making feather falling.... better.
	 * If your boots have Feather Falling IV or higher, instead of taking
	 * reduced fall damage, this handler will negate all fall damage completely.
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerTweak(PlayerEvent event)
	{
		// Checks the boot itemstack. Why are boots 0? You'd think it'd start
		// from the helmet...
		ItemStack boots = event.entityPlayer.getCurrentArmor(0);
		// Gets the Feather Falling enchantment level on your boots
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, boots);
		// Does the thing!
		if (boots != null && EnchantmentLevelArmor >= 4)
			event.entityPlayer.fallDistance = 0.0F;
	}
}