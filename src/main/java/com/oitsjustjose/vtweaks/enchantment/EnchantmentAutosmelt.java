package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;

public class EnchantmentAutosmelt extends Enchantment
{

	protected EnchantmentAutosmelt()
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND});
		Enchantment.REGISTRY.register(VTweaks.config.autosmeltID, new ResourceLocation(VTweaks.MODID, "autosmelt"), this);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		if (enchantment == Enchantments.getEnchantment("silk_touch"))
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