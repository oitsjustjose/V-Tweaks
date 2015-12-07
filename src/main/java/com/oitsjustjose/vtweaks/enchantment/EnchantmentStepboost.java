package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentStepboost extends Enchantment
{
	public EnchantmentStepboost(int id)
	{
		super(id, new ResourceLocation("stepboost"), 1, EnumEnchantmentType.ARMOR_FEET);
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 1 + 10 * (par1 - 1);
	}

	@Override
	public int getMaxEnchantability(int p_77317_1_)
	{
		return super.getMinEnchantability(p_77317_1_) + 50;
	}

	@Override
	public int getMaxLevel()
	{
		return 1;
	}
}