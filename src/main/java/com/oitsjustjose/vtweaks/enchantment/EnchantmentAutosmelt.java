package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class EnchantmentAutosmelt extends Enchantment
{

	protected EnchantmentAutosmelt()
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		if (enchantment == Enchantment.getEnchantmentByID(33))
			return false;
		return true;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTool;
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