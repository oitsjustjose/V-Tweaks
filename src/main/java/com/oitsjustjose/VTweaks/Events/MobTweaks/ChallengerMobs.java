package com.oitsjustjose.VTweaks.Events.MobTweaks;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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

public class ChallengerMobs
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerEvent(LivingSpawnEvent event)
	{
		// Local Variables
		World world = event.world;
		int rand = event.world.rand.nextInt(8);

		// Checks if there IS an entity, and then whether it's an enemy
		if (event.entity != null && event.entity instanceof EntityMob)
		{
			if (event.entity instanceof EntityPigZombie)
				return;
			// Local Variable of the Event's Entity casted to type Mob
			EntityMob monster = (EntityMob) event.entity;

			// Simple way of doing a "1 in x" chance, where x is the other variable
			if (0 == world.rand.nextInt(Config.challengerMobRarity))
			{
				// Makes mighty zombies tanky
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
					((ItemArmor) helmet.getItem()).func_82813_b(helmet, world.rand.nextInt(1677215));
					monster.setCurrentItemOrArmor(1, null);
					monster.setCurrentItemOrArmor(2, null);
					monster.setCurrentItemOrArmor(3, null);
					monster.setCurrentItemOrArmor(4, helmet);
				}

				// Makes all special mobs fire resistant. They need this
				monster.addPotionEffect(new PotionEffect(Potion.fireResistance.id, Short.MAX_VALUE, 8));

				// Makes all special mobs more difficult to damage, especially resilient zombies
				if (rand == 6)
					monster.addPotionEffect(new PotionEffect(Potion.resistance.id, Short.MAX_VALUE, 3));
				else
					monster.addPotionEffect(new PotionEffect(Potion.resistance.id, Short.MAX_VALUE, 2));

				// Makes Hyper mobs... Hyper
				if (rand == 7)
					monster.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Short.MAX_VALUE, 4));

				// Sets a nice name above their head! Helpful for their drops later, too.
				monster.setCustomNameTag(className(rand, monster));

				// This doesn't really work.. not sure why.
				for (int i = 0; i < 5; i++)
					monster.setEquipmentDropChance(i, 0.0F);
			}
		}
	}

	// A simple method for essentially assigning a class to a challenger mob
	ItemStack toolForMobClass(int type)
	{
		// Item initialization
		ItemStack bow = new ItemStack(Items.bow);
		ItemStack hungryBowl = new ItemStack(Items.bowl);
		ItemStack opSword = new ItemStack(Items.diamond_sword);
		ItemStack wand = new ItemStack(Items.stick);
		ItemStack firework = new ItemStack(Items.fireworks);
		ItemStack sign = new ItemStack(Items.sign);

		// Don't want it to drop with TOO Much durability left!
		opSword.setItemDamage(opSword.getMaxDamage() / 8);

		// Adds enchantments to all itemstacks
		opSword.addEnchantment(Enchantment.sharpness, 3);
		hungryBowl.addEnchantment(Enchantment.sharpness, 8);
		firework.addEnchantment(Enchantment.fireAspect, 5);
		wand.addEnchantment(Enchantment.fireAspect, 1);
		wand.addEnchantment(Enchantment.knockback, 2);
		sign.addEnchantment(Enchantment.knockback, 10);

		// Proper return types; notice two zombies don't get anything special
		// One of these is the hyper one, the other is the resilient
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

	// NameTag Creator, basically!
	public String className(int type, EntityMob mob)
	{
		// This finds the mob's basic in-code class name
		String mobString = mob.toString();
		// This part cuts out all the extra crap following "EntityZombie"
		mobString = mobString.substring(0, mobString.indexOf("["));

		// This part splits it apart at the capital letters
		String[] nameParts = mobString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		// Resetting the mobString
		mobString = "";
		// This part glues them back together
		for (int i = 0; i < nameParts.length; i++)
		{
			// But only if the part doesn't contain the word "entity", no need
			// To tell the player what they already know!!
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
		// Returns the prefix (configurable) with the mob's formal name appended
		return Config.challengerMobs[type] + " " + mobString;
	}

}