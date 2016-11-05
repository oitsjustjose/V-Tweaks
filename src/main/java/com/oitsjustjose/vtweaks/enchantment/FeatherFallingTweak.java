package com.oitsjustjose.vtweaks.enchantment;

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
		if (!(event.getEntity() instanceof EntityPlayer))
			return;

		EntityPlayer player = (EntityPlayer) event.getEntity();

		if (player.inventory.armorInventory[0] == null)
			return;

		if (player.inventory.armorInventory[0].getMetadata() > player.inventory.armorInventory[0].getMaxDamage() || player.inventory.armorInventory[0].stackSize != 1)
			player.inventory.armorInventory[0] = null;

		ItemStack boots = player.inventory.armorInventory[0];

		if (boots != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("feather_falling"), boots) >= 4)
			if (event.getSource() == DamageSource.fall)
			{
				boots.damageItem((int) event.getAmount(), player);
				event.setAmount(0.0F);
				if (boots.getMetadata() > boots.getMaxDamage() || boots.stackSize != 1)
					player.inventory.armorInventory[0] = null;
			}
	}
}