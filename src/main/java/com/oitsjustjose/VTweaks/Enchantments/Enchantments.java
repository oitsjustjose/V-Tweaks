package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.VTweaks;
import com.oitsjustjose.VTweaks.Util.Config;

import net.minecraft.enchantment.Enchantment;

public class Enchantments
{
	public static Enchantment hyperMending;
	public static Enchantment autosmelt;
	public static Enchantment stepboost;
	public static Enchantment lumbering;

	public static void initialize()
	{
		if (Config.hypermendingEnchantmentID != 0)
		{
			hyperMending = new EnchantmentHypermending(Config.hypermendingEnchantmentID).setName(VTweaks.modid + "_hyperMending");
			Enchantment.addToBookList(hyperMending);
		}

		if (Config.autosmeltEnchantmentID != 0)
		{
			autosmelt = new EnchantmentAutosmelt(Config.autosmeltEnchantmentID).setName(VTweaks.modid + "_autosmelt");
			Enchantment.addToBookList(autosmelt);
		}

		if (Config.stepboostEnchantmentID != 0)
		{
			stepboost = new EnchantmentStepboost(Config.stepboostEnchantmentID).setName(VTweaks.modid + "_stepboost");
			Enchantment.addToBookList(stepboost);
		}

		if (Config.lumberingEnchantmentID != 0)
		{
			lumbering = new EnchantmentLumbering(Config.lumberingEnchantmentID).setName(VTweaks.modid + "_lumbering");
			Enchantment.addToBookList(lumbering);
		}

	}
}