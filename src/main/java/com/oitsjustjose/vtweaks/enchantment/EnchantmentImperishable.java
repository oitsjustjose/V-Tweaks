package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentImperishable extends Enchantment
{
	protected EnchantmentImperishable()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.values());
		Enchantment.REGISTRY.register(VTweaks.config.imperishableID, new ResourceLocation(VTweaks.MODID, "imperishable"), this);
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