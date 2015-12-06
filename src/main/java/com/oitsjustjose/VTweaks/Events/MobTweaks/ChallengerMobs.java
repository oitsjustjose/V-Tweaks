package com.oitsjustjose.VTweaks.Events.MobTweaks;

import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChallengerMobs
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerEvent(LivingSpawnEvent event)
	{
		World world = event.world;
		int rand = event.world.rand.nextInt(8);

		if (event.entity != null && event.entity instanceof EntityMob)
		{
			if (event.entity instanceof EntityPigZombie)
				return;
			EntityMob monster = (EntityMob) event.entity;

			if (0 == world.rand.nextInt(Config.challengerMobRarity))
			{
				if (rand == 0)
				{
					monster.setCurrentItemOrArmor(0, toolForMobClass(rand));
					monster.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Short.MAX_VALUE, 1));
					monster.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
					monster.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
					monster.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
					monster.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));
				}
				else
				{
					monster.setCurrentItemOrArmor(0, toolForMobClass(rand));
					ItemStack helmet = new ItemStack(Items.leather_helmet);
					((ItemArmor) helmet.getItem()).setColor(helmet, world.rand.nextInt(1677215));
					monster.setCurrentItemOrArmor(1, null);
					monster.setCurrentItemOrArmor(2, null);
					monster.setCurrentItemOrArmor(3, null);
					monster.setCurrentItemOrArmor(4, helmet);
				}

				if (rand == 6)
					monster.addPotionEffect(new PotionEffect(Potion.resistance.id, Short.MAX_VALUE, 3));
				else
					monster.addPotionEffect(new PotionEffect(Potion.resistance.id, Short.MAX_VALUE, 2));

				if (rand == 7)
					monster.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Short.MAX_VALUE, 4));

				monster.setCustomNameTag(mobClassName(rand, monster));
				monster.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Short.MAX_VALUE, 8));

				for (int i = 0; i < 5; i++)
					monster.setEquipmentDropChance(i, 0.0F);
			}
		}
	}

	ItemStack toolForMobClass(int type)
	{
		ItemStack bow = new ItemStack(Items.bow);
		ItemStack hungryBowl = new ItemStack(Items.bowl);
		ItemStack opSword = new ItemStack(Items.diamond_sword);
		ItemStack wand = new ItemStack(Items.stick);
		ItemStack firework = new ItemStack(Items.fireworks);
		ItemStack sign = new ItemStack(Items.sign);

		opSword.setItemDamage(opSword.getMaxDamage() / 8);

		opSword.addEnchantment(Enchantment.sharpness, 3);
		hungryBowl.addEnchantment(Enchantment.sharpness, 8);
		firework.addEnchantment(Enchantment.fireAspect, 5);
		wand.addEnchantment(Enchantment.fireAspect, 1);
		wand.addEnchantment(Enchantment.knockback, 2);
		sign.addEnchantment(Enchantment.knockback, 10);

		switch (type)
		{
		case 0:
			return opSword;
		case 1:
			return hungryBowl;
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