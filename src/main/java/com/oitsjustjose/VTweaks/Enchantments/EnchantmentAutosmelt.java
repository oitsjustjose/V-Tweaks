package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

public class EnchantmentAutosmelt extends Enchantment
{
	public EnchantmentAutosmelt(int id)
	{
		super(id, new ResourceLocation("autosmelt"), 1, EnumEnchantmentType.DIGGER);
	}

	// Makes this enchantment compatible with any other enchants that wouldn't
	// conflict.
	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		if (enchantment == Enchantment.silkTouch)
			return false;
		return true;
	}

	// Makes it so it can only be applied to a pickaxe
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemTool;
	}

	// Makes it so you require higher level enchants to acquire this
	// enchantment.
	@Override
	public int getMinEnchantability(int par1)
	{
		return 0;
	}

	// Makes it so you require higher level enchants to acquire this
	// enchantment.
	@Override
	public int getMaxEnchantability(int par1)
	{
		return 0;
	}
}