package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FeatherFallingTweak
{
	@SubscribeEvent
	public void registerTweak(LivingHurtEvent event)
	{
		// Check if enchantment is disabled
		if (!VTweaks.config.enableFeatherFallingTweak)
			return;
		// Ensures we're working on a player entity AND we're working with fall damage
		if (!(event.getEntity() instanceof EntityPlayer) || event.getSource() != DamageSource.fall)
			return;

		EntityPlayer player = (EntityPlayer) event.getEntity();

		// Checks if boots are worn
		if (player.inventory.armorInventory[0] == null)
			return;

		ItemStack boots = player.inventory.armorInventory[0];
		// Checks if FeatherFalling IV or higher is on the boots
		if (EnchantmentHelper.getEnchantmentLevel(HelperFunctions.getEnchantment("minecraft", "feather_falling"), boots) >= 4)
		{
			boots.damageItem((int) event.getAmount(), player);
			event.setAmount(0.0F);
			// Normalizes armor inventory stack when damaged
			if (boots.getMetadata() > boots.getMaxDamage() || boots.stackSize != 1)
				player.inventory.armorInventory[0] = null;
		}
	}
}