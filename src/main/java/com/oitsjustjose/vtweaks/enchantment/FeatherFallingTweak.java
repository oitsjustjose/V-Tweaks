package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
		ItemStack boots = player.inventory.armorInventory.get(0);

		if (event.getSource() != DamageSource.FALL || boots == ItemStack.EMPTY)
			return;

		if (boots.getMetadata() > boots.getMaxDamage() || boots.getCount() != 1)
		{
			player.inventory.armorInventory.set(0, new ItemStack((Item) null));
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("feather_falling"), boots) >= 4)
		{
			boots.damageItem((int) event.getAmount(), player);
			event.setAmount(0.0F);
			if (boots.getMetadata() > boots.getMaxDamage() || boots.getCount() != 1)
				boots = ItemStack.EMPTY;
		}
	}
}