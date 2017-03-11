package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentStepboost extends Enchantment
{
	protected EnchantmentStepboost()
	{
		super(Rarity.UNCOMMON, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
		Enchantment.REGISTRY.register(VTweaks.config.stepboostID, new ResourceLocation(VTweaks.MODID, "stepboost"), this);
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