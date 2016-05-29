package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.util.LogHelper;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentPiercingHandler
{
	int damageType;

	@SubscribeEvent
	public void register(AttackEntityEvent event)
	{
		if (event.getEntityPlayer() == null || event.getTarget() == null || event.getEntityPlayer().getHeldItemMainhand() == null)
			return;

		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		ItemStack heldItem = player.getHeldItemMainhand();
		DamageSource piercing = new EntityDamageSource("player", player).setDamageBypassesArmor();

		if (EnchantmentHelper.getEnchantmentLevel(Enchantments.piercing, heldItem) > 0)
		{
			float amount = 0.0F;
			int dmgEnchantLvl = 0;

			if (heldItem.getItem() instanceof ItemSword)
			{
				ItemSword weapon = (ItemSword) heldItem.getItem();
				amount = (weapon.getDamageVsEntity() * 2) + 1;
			}
			else if (heldItem.getItem() instanceof ItemAxe)
			{
				ItemAxe weapon = (ItemAxe) heldItem.getItem();
				amount = weapon.getToolMaterial().getDamageVsEntity();
			}
			if (target instanceof EntityCreature)
			{
				EntityCreature creature = (EntityCreature) target;
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("sharpness"), heldItem) > 0)
				{
					dmgEnchantLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("sharpness"), heldItem);
					damageType = 0;
				}
				else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("smite"), heldItem) > 0)
				{
					dmgEnchantLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("sharpness"), heldItem);
					damageType = 1;
				}
				else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("bane_of_arthropods"), heldItem) > 0)
				{
					dmgEnchantLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.getEnchantment("sharpness"), heldItem);
					damageType = 2;
				}
				amount += calcDamageByCreature(dmgEnchantLvl, creature.getCreatureAttribute());
			}

			event.setCanceled(true);
			event.getEntityPlayer().swingArm(EnumHand.MAIN_HAND);
			heldItem.damageItem(2, player);
			target.attackEntityFrom(piercing, amount);
			player.getEntityWorld().playSound(player, player.getPosition(), SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.player.attack.crit")), SoundCategory.PLAYERS, 1.0F, 1.0F);
			LogHelper.info(">>" + amount);
		}
	}

	float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
	{
		if (this.damageType == 0)
			return 1.0F + (float) Math.max(0, level - 1) * 0.5F;
		else if (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD)
			return (float) level * 2.5F;
		else if (this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD)
			return (float) level * 2.5F;
		else
			return 0.0F;
	}
}