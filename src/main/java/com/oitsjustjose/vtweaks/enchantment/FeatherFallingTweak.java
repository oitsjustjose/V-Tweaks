package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
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
		if (!(event.entity instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) event.entity;

		if (player.getCurrentArmor(0).getMetadata() > player.getCurrentArmor(0).getMaxDamage() || player.getCurrentArmor(0).stackSize != 1)
			player.setCurrentItemOrArmor(1, null);

		ItemStack boots = player.getCurrentArmor(0);

		if (boots != null && EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, boots) >= 4)
			if (event.source == DamageSource.fall)
			{
				boots.damageItem((int) event.ammount, player);
				event.ammount = 0.0F;
				if (boots.getMetadata() > boots.getMaxDamage() || boots.stackSize != 1)
					player.setCurrentItemOrArmor(1, null);
			}
	}
}