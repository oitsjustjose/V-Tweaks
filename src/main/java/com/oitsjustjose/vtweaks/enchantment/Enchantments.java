package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;

public class Enchantments
{
	public static Enchantment hypermending;
	public static Enchantment autosmelt;
	public static Enchantment stepboost;
	public static Enchantment lumbering;
	public static Enchantment imperishable;

	public static void initialize()
	{
		if (VTweaks.config.hypermendingID != 0)
		{
			hypermending = new EnchantmentHypermending().setName(VTweaks.MODID + ":hypermending");
		}

		if (VTweaks.config.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt().setName(VTweaks.MODID + ":autosmelt");
		}

		if (VTweaks.config.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost().setName(VTweaks.MODID + ":stepboost");
		}

		if (VTweaks.config.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering().setName(VTweaks.MODID + ":lumbering");
		}

		if (VTweaks.config.imperishableID != 0)
		{
			imperishable = new EnchantmentImperishable().setName(VTweaks.MODID + ":imperishable");
		}
	}
}