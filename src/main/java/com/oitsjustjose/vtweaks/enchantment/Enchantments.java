package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public class Enchantments
{
	public static Enchantment hyperMending;
	public static Enchantment autosmelt;
	public static Enchantment stepboost;
	public static Enchantment lumbering;

	public static void initialize()
	{
		if (VTweaks.config.hypermendingID != 0)
		{
			hyperMending = new EnchantmentHypermending().setName(VTweaks.MODID + ":hypermending");
			Enchantment.REGISTRY.register(VTweaks.config.hypermendingID, new ResourceLocation(VTweaks.MODID, "hypermending"), hyperMending);
		}

		if (VTweaks.config.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt().setName(VTweaks.MODID + ":autosmelt");
			Enchantment.REGISTRY.register(VTweaks.config.autosmeltID, new ResourceLocation(VTweaks.MODID, "autosmelt"), autosmelt);
		}

		if (VTweaks.config.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost().setName(VTweaks.MODID + ":stepboost");
			Enchantment.REGISTRY.register(VTweaks.config.stepboostID, new ResourceLocation(VTweaks.MODID, "stepboost"), stepboost);
		}

		if (VTweaks.config.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering().setName(VTweaks.MODID + ":lumbering");
			Enchantment.REGISTRY.register(VTweaks.config.lumberingID, new ResourceLocation(VTweaks.MODID, "lumbering"), lumbering);
		}
	}
	
	public static Enchantment getEnchantment(String enchName)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(enchName));
	}
	
	public static Enchantment getEnchantment(String modid, String enchName)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(modid, enchName));
	}
}