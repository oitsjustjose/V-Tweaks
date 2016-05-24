package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentHypermending extends Enchantment
{
	protected EnchantmentHypermending()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
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