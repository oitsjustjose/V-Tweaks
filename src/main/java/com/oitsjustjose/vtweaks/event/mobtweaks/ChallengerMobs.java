package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChallengerMobs
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerEvent(LivingSpawnEvent event)
	{
		if(!VTweaks.config.enableChallengerMobs)
			return;
		
		if (!event.getWorld().isRemote)
		{
			if (0 == event.getWorld().rand.nextInt(VTweaks.config.challengerMobRarity))
			{
				final int rand = event.getWorld().rand.nextInt(8);

				if (event.getEntity() != null && event.getEntity() instanceof EntityMob)
				{
					if (event.getEntity() instanceof EntityPigZombie)
						return;

					EntityMob monster = (EntityMob) event.getEntity();
					monster.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
					monster.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
					monster.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
					monster.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);

					// Custom Name Tags, and infinite fire resistance to prevent cheesy kills
					monster.setCustomNameTag(mobClassName(rand, monster));
					PotionEffect p = new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("fire_resistance")), Integer.MAX_VALUE, 0, true, true);
					monster.addPotionEffect(p);
					// Every challenger mob will have a main hand item. Done before any checks.
					monster.setHeldItem(EnumHand.MAIN_HAND, toolForMobClass(rand));
					monster.setDropChance(EntityEquipmentSlot.MAINHAND, Float.MIN_VALUE);

					monster.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(getMobSpeed(rand));
					monster.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMobHealth(rand));
					monster.setHealth(getMobHealth(rand));

					// Special Man Pants for Zestonian Mobs
					if (rand == 5)
					{
						ItemStack pants = new ItemStack(Items.GOLDEN_LEGGINGS);
						pants.setStackDisplayName("Man Pants");
						pants.addEnchantment(Enchantments.getEnchantment("blast_protection"), 5);
						monster.setItemStackToSlot(EntityEquipmentSlot.LEGS, pants);
					}

				}
			}
		}
	}

	@SubscribeEvent
	public void registerEvent(LivingDropsEvent event)
	{
		if(!VTweaks.config.enableChallengerMobs)
			return;
		
		if (event.getEntity() != null && event.getEntity() instanceof EntityMob)
			if (isChallengerMob((EntityMob) event.getEntity()))
				for (int j = 0; j < 2; j++)
					event.getDrops().add(getItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ));
	}

	double getMobSpeed(int type)
	{
		switch (type)
		{
		case 0:
			return 0.18D;
		case 7:
			return 0.6D;
		default:
			return 0.25;
		}
	}

	float getMobHealth(int type)
	{
		switch (type)
		{
		case 0:
			return 80F;
		case 6:
			return 160F;
		case 7:
			return 10F;
		default:
			return 40F;
		}
	}

	ItemStack toolForMobClass(int type)
	{
		ItemStack[] r = new ItemStack[] { new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.BOWL), new ItemStack(Items.BOW), new ItemStack(Items.STICK), new ItemStack(Items.FIREWORKS), new ItemStack(Items.SIGN) };

		r[0].setItemDamage(r[0].getMaxDamage() / 8);
		r[0].addEnchantment(Enchantments.getEnchantment("sharpness"), 3);
		r[1].addEnchantment(Enchantments.getEnchantment("sharpness"), 10);
		r[2].addEnchantment(Enchantments.getEnchantment("punch"), 2);
		r[2].addEnchantment(Enchantments.getEnchantment("power"), 3);
		r[3].addEnchantment(Enchantments.getEnchantment("fire_aspect"), 1);
		r[3].addEnchantment(Enchantments.getEnchantment("knockback"), 2);
		r[4].addEnchantment(Enchantments.getEnchantment("fire_aspect"), 5);
		r[5].addEnchantment(Enchantments.getEnchantment("knockback"), 10);

		if (type <= (r.length - 1))
			return r[type];
		else
			return ItemStack.EMPTY;
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
		return VTweaks.config.challengerMobNames[type] + " " + mobString;
	}

	EntityItem getItem(World world, double x, double y, double z)
	{
		int RNG = world.rand.nextInt(VTweaks.config.challengerLootTable.size());
		ItemStack temp = VTweaks.config.challengerLootTable.get(RNG).copy();
		return new EntityItem(world, x, y, z, temp.copy());
	}

	boolean isChallengerMob(EntityMob entity)
	{
		String n = entity.getCustomNameTag().toLowerCase();
		if (n == null)
			return false;

		String[] preFixes = VTweaks.config.challengerMobNames.clone();
		for (int i = 0; i < preFixes.length; i++)
			if (n.contains(preFixes[i].toLowerCase()))
				return true;
		return false;
	}
}