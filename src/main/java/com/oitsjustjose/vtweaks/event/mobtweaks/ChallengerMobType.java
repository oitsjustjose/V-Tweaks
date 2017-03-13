package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum ChallengerMobType
{
	MIGHTY(0.18D, 80F, "Mighty"), HUNGRY(0.25D, 40F, "Hungry"), RANGER(0.25D, 40F, "Ranger"), ARCANE(0.25D, 40F, "Arcane"), PYRO(0.25D, 40F, "Pyro"), ZESTONIAN(0.25D, 40F, "Zestonian"), REINFORCED(0.25D, 160F, "Reinforced"), AGILE(0.6D, 10F, "Agile");

	private final double mobSpeed;
	private final float mobHealth;
	private final ItemStack heldItem;
	private final String commonName;

	private ChallengerMobType(double speed, float health, String name)
	{
		this.mobSpeed = speed;
		this.mobHealth = health;
		this.heldItem = toolForMobClass(this.ordinal());
		this.commonName = name;
	}

	ItemStack toolForMobClass(int type)
	{
		ItemStack[] toolList = new ItemStack[] { new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.BOWL), new ItemStack(Items.BOW), new ItemStack(Items.STICK), new ItemStack(Items.FIREWORKS), new ItemStack(Items.SIGN), null, null };

		toolList[0].setItemDamage(toolList[0].getMaxDamage() / 8);
		toolList[0].addEnchantment(Enchantments.getEnchantment("sharpness"), 3);
		toolList[1].addEnchantment(Enchantments.getEnchantment("sharpness"), 10);
		toolList[2].addEnchantment(Enchantments.getEnchantment("punch"), 2);
		toolList[2].addEnchantment(Enchantments.getEnchantment("power"), 3);
		toolList[3].addEnchantment(Enchantments.getEnchantment("fire_aspect"), 1);
		toolList[3].addEnchantment(Enchantments.getEnchantment("knockback"), 2);
		toolList[4].addEnchantment(Enchantments.getEnchantment("fire_aspect"), 5);
		toolList[5].addEnchantment(Enchantments.getEnchantment("knockback"), 10);

		return toolList[type];
	}

	public double getSpeed()
	{
		return this.mobSpeed;
	}

	public float getHealth()
	{
		return this.mobHealth;
	}

	public ItemStack getEquipment()
	{
		return this.heldItem;
	}

	public String getPrefix()
	{
		return this.commonName;
	}
}