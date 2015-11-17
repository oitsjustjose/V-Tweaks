package com.oitsjustjose.VTweaks.Events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class ChallengerMobs
{
	int rand;

	@SubscribeEvent
	public void registerEvent(EntityJoinWorldEvent event)
	{
		World world = event.world;
		rand = event.world.rand.nextInt(8);
		
		if(event.entity != null && event.entity instanceof EntityMob)
		{
			EntityMob monster = (EntityMob)event.entity;
			
			//if(world.rand.nextInt(100) == 50)
			if(true)
			{
				monster.setHealth(100.0F);
				monster.setCurrentItemOrArmor(0, toolForMobClass(rand));
				if(rand == 0)
					monster.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Short.MAX_VALUE, 1));
				if(rand == 6)
					monster.addPotionEffect(new PotionEffect(Potion.regeneration.id, Short.MAX_VALUE, 2));
				if(rand == 7)
					monster.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, Short.MAX_VALUE, 4));
				monster.setCustomNameTag(className(rand, monster));
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
		
		//Don't want it to drop with TOO Much durability left!
		opSword.setItemDamage(opSword.getMaxDamage() / 4);
		
		opSword.addEnchantment(Enchantment.sharpness, 3);
		hungryBowl.addEnchantment(Enchantment.sharpness, 8);
		firework.addEnchantment(Enchantment.fireAspect, 5);
		wand.addEnchantment(Enchantment.fireAspect, 1);
		wand.addEnchantment(Enchantment.knockback, 2);
		sign.addEnchantment(Enchantment.knockback, 10);
		
		switch(type)
		{
		case 0:return opSword;
		case 1:return hungryBowl;
		case 2:return bow;
		case 3:return wand;
		case 4:return firework;
		case 5:return sign;
		default:return null;
		}
	}
	
	String className(int type, EntityMob mob)
	{
		String mobString = mob.toString();
		String preString = "";
		mobString = mobString.replace("entity.", "").replace(".name.", "").trim();
		
		switch(type)
		{
		case 0:preString = "Mighty";
		case 1:preString = "Hungry";
		case 2:preString = "Ranger";
		case 3:preString = "Mage";
		case 4:preString = "Pyro";
		case 5:preString = "Zestonian";
		case 6:preString = "Resilient";
		case 7:preString = "Hyper";
		}
		
		return preString + " " + mobString;
	}
	
}