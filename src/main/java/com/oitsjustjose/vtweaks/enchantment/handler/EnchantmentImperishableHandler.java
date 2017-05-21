package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentImperishableHandler
{
	// This event is for tools
	@SubscribeEvent
	public void register(PlayerInteractEvent event)
	{
		if (VTweaks.config.imperishableID <= 0)
			return;

		if (event.getItemStack().isEmpty() || event.getEntityPlayer() == null)
			return;

		ItemStack stack = event.getItemStack();

		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.imperishable, stack) > 0)
		{
			if (stack.getItemDamage() >= stack.getMaxDamage())
			{
				// Assuming it's a bed item named "Sleeping Bag":
				if (event.isCancelable())
					event.setCanceled(true);
				event.setResult(Result.DENY);
			}
		}
	}

	// This event is for attacking / damage
	@SubscribeEvent
	public void register(LivingHurtEvent event)
	{
		if (VTweaks.config.imperishableID <= 0)
			return;

		if (event.getEntityLiving() == null || event.getSource() == null || event.getSource().getEntity() == null)
			return;

		if (event.getSource().getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
			ItemStack stack = player.getHeldItemMainhand();

			if (stack.isEmpty())
				return;

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.imperishable, stack) > 0)
			{
				if (stack.getItemDamage() >= stack.getMaxDamage())
				{
					stack.attemptDamageItem(-1, player.getRNG());

					if (event.isCancelable())
						event.setCanceled(true);
					event.setResult(Result.DENY);
					event.setAmount(0.0F);
				}
			}
		}
		else if (event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();

			float validDefense = 0.0F;
			float validToughness = 0.0F;
			for (ItemStack stack : player.getEquipmentAndArmor())
			{

				
				if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor))
					continue;

				// How much the player is ACTUALLY getting hurt
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.imperishable, stack) > 0)
				{
					if (stack.getItemDamage() >= stack.getMaxDamage())
					{
						System.out.println(event.getAmount());
						stack.attemptDamageItem(-1, player.getRNG());
						continue;
					}
				}

				ItemArmor armor = (ItemArmor) stack.getItem();
				validDefense += armor.damageReduceAmount;
				validToughness += armor.toughness;
			}

			float damage = CombatRules.getDamageAfterAbsorb(event.getAmount(), validDefense, validToughness);
			event.setAmount(damage);
		}
	}
}
