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

	// Makes this enchantment compatible with any other enchants
	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		return true;
	}

	// Makes it so you cannot get this enchantment by standard enchanting
	@Override
	public int getMinEnchantability(int par1)
	{
		return 0;
	}

	// Makes it so you cannot get this enchantment by standard enchanting
	@Override
	public int getMaxEnchantability(int par1)
	{
		return 0;
	}
}