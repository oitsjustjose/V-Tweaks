package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChallengerMobs
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerEvent(LivingSpawnEvent event)
	{
		Random random = new Random();
		int rand = random.nextInt(8);
		if (0 == random.nextInt(VTweaks.modConfig.challengerMobRarity))
		{
			if (event.getEntity() != null && event.getEntity() instanceof EntityMob)
			{
				if (event.getEntity() instanceof EntityPigZombie)
					return;

				EntityMob monster = (EntityMob) event.getEntity();

				if (rand == 0)
				{
					monster.setHeldItem(EnumHand.MAIN_HAND, toolForMobClass(rand));
					monster.addPotionEffect(new PotionEffect(Potion.getPotionById(2), Short.MAX_VALUE, 1));
				}
				else
				{
					monster.setHeldItem(EnumHand.MAIN_HAND, toolForMobClass(rand));
					
					ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
					((ItemArmor) helmet.getItem()).setColor(helmet, random.nextInt(1677215));
					
					monster.setItemStackToSlot(EntityEquipmentSlot.HEAD, helmet);
					monster.setItemStackToSlot(EntityEquipmentSlot.CHEST, null);
					monster.setItemStackToSlot(EntityEquipmentSlot.LEGS, null);
					monster.setItemStackToSlot(EntityEquipmentSlot.FEET, null);
				}

				if (rand == 0)
				{
					monster.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80F);
					monster.setHealth(80F);
				}
				else if (rand == 6)
				{
					monster.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(160F);
					monster.setHealth(160F);
				}
				else if (rand == 7)
				{
					monster.addPotionEffect(new PotionEffect(Potion.getPotionById(1), Short.MAX_VALUE, 4));
				}
				else
				{
					monster.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40F);
					monster.setHealth(40F);
				}

				monster.setCustomNameTag(mobClassName(rand, monster));
				monster.addPotionEffect(new PotionEffect(Potion.getPotionById(12), Short.MAX_VALUE, 8));
			}
		}

	}

	ItemStack toolForMobClass(int type)
	{
		ItemStack bow = new ItemStack(Items.BOW);
		ItemStack bowl = new ItemStack(Items.BOWL);
		ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
		ItemStack wand = new ItemStack(Items.STICK);
		ItemStack firework = new ItemStack(Items.FIREWORKS);
		ItemStack sign = new ItemStack(Items.SIGN);

		sword.setItemDamage(sword.getMaxDamage() / 8);

		bow.addEnchantment(Enchantment.getEnchantmentByID(48), 2);
		bow.addEnchantment(Enchantment.getEnchantmentByID(49), 3);
		sword.addEnchantment(Enchantment.getEnchantmentByID(16), 3);
		bowl.addEnchantment(Enchantment.getEnchantmentByID(16), 8);
		firework.addEnchantment(Enchantment.getEnchantmentByID(20), 5);
		wand.addEnchantment(Enchantment.getEnchantmentByID(20), 1);
		wand.addEnchantment(Enchantment.getEnchantmentByID(19), 2);
		sign.addEnchantment(Enchantment.getEnchantmentByID(19), 10);

		switch (type)
		{
		case 0:
			return sword;
		case 1:
			return bowl;
		case 2:
			return bow;
		case 3:
			return wand;
		case 4:
			return firework;
		case 5:
			return sign;
		default:
			return null;
		}
	}

	public String mobClassName(int type, EntityMob mob)
	{
		String mobString = mob.toString().substring(0, mob.toString().indexOf("["));
		String[] nameParts = mobString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		mobString = "";
		for (int i = 0; i < nameParts.length; i++)
		{
			if (nameParts[i].toLowerCase().contains("entity"))
				mobString += "";
			else
			{
				if (i != (nameParts.length - 1))
					mobString += nameParts[i] + " ";
				else
					mobString += nameParts[i];
			}
		}
		return VTweaks.modConfig.challengerMobs[type] + " " + mobString;
	}
}