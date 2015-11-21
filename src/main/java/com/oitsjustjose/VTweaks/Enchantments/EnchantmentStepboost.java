package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentStepboost extends Enchantment
{
	public EnchantmentStepboost(int id)
	{
		super(id, 1, EnumEnchantmentType.armor_feet);
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