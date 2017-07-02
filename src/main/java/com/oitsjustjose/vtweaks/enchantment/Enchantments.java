package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;

public class Enchantments
{
	public Enchantment hypermending;
	public Enchantment autosmelt;
	public Enchantment stepboost;
	public Enchantment lumbering;
	public Enchantment imperishable;

	public Enchantments()
	{
		init();
	}
	
	public void init()
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