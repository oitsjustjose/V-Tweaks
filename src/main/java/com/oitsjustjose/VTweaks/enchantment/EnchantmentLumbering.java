package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentLumbering extends Enchantment
{
	public EnchantmentLumbering(int id)
	{
		super(id, new ResourceLocation("lumbering"), 1, EnumEnchantmentType.DIGGER);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		return true;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemAxe;
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
