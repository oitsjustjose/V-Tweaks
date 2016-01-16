package com.oitsjustjose.vtweaks.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
		if (0 == random.nextInt(Config.challengerMobRarity))
		{
			if (event.entity != null && event.entity instanceof EntityMob)
			{
				if (event.entity instanceof EntityPigZombie)
					return;

				EntityMob monster = (EntityMob) event.entity;

				if (rand == 0)
				{
					monster.setCurrentItemOrArmor(0, toolForMobClass(rand));
					monster.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Short.MAX_VALUE, 1));
				}
				else
				{
					monster.setCurrentItemOrArmor(0, toolForMobClass(rand));
					ItemStack helmet = new ItemStack(Items.leather_helmet);
					((ItemArmor) helmet.getItem()).setColor(helmet, random.nextInt(1677215));
					monster.setCurrentItemOrArmor(1, null);
					monster.setCurrentItemOrArmor(2, null);
					monster.setCurrentItemOrArmor(3, null);
					monster.setCurrentItemOrArmor(4, helmet);

				}

				if (rand == 0)
				{
					monster.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80F);
					monster.setHealth(80F);
				}
				else if (rand == 6)
				{
					monster.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(160F);
					monster.setHealth(160F);
				}
				else if (rand == 7)
				{
					monster.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Short.MAX_VALUE, 4));
				}
				else
				{
					monster.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40F);
					monster.setHealth(40F);
				}

				monster.setCustomNameTag(mobClassName(rand, monster));
				monster.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Short.MAX_VALUE, 8));
			}
		}

	}

	public static boolean isChallengerMob(EntityMob entity)
	{
		String n = entity.getCustomNameTag().toLowerCase();
		if (n == null)
			return false;

		String[] preFixes = Config.challengerMobs.clone();
		for (int i = 0; i < preFixes.length; i++)
			if (n.contains(preFixes[i].toLowerCase()))
				if (entity.isPotionActive(Potion.fireResistance))
					return true;
		return false;
	}

	ItemStack toolForMobClass(int type)
	{
		ItemStack bow = new ItemStack(Items.bow);
		ItemStack bowl = new ItemStack(Items.bowl);
		ItemStack sword = new ItemStack(Items.diamond_sword);
		ItemStack wand = new ItemStack(Items.stick);
		ItemStack firework = new ItemStack(Items.fireworks);
		ItemStack sign = new ItemStack(Items.sign);

		sword.setItemDamage(sword.getMaxDamage() / 8);

		bow.addEnchantment(Enchantment.power, 2);
		bow.addEnchantment(Enchantment.punch, 3);
		sword.addEnchantment(Enchantment.sharpness, 3);
		bowl.addEnchantment(Enchantment.sharpness, 8);
		firework.addEnchantment(Enchantment.fireAspect, 5);
		wand.addEnchantment(Enchantment.fireAspect, 1);
		wand.addEnchantment(Enchantment.knockback, 2);
		sign.addEnchantment(Enchantment.knockback, 10);

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
		return Config.challengerMobs[type] + " " + mobString;
	}
}