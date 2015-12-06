package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentHypermending extends Enchantment
{
	public EnchantmentHypermending(int id)
	{
		super(id, new ResourceLocation("hypermending"), 1, EnumEnchantmentType.BREAKABLE);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		return true;
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 0;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return 0;
	}
}