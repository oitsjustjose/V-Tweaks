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
		// Initializes unbreakable if the enchantment ID isn't set to 0
		if (Config.hypermendingEnchantmentID != 0)
		{
			// Initializes the unbreakable variable
			hyperMending = new EnchantmentHypermending(Config.hypermendingEnchantmentID).setName(VTweaks.modid
					+ "_hyperMending");
			// Adds said enchantment to the Enchanted Books list. Otherwise an
			// enchanted book with unbreakable would only be spawn-able.
			Enchantment.addToBookList(hyperMending);
		}

		// Initializes autosmelt if the enchantment ID isn't set to 0
		if (Config.autosmeltEnchantmentID != 0)
		{
			// Initializes the autosmelt variable
			autosmelt = new EnchantmentAutosmelt(Config.autosmeltEnchantmentID).setName(VTweaks.modid + "_autosmelt");
			// Adds said enchantment to the Enchanted Books list. Otherwise an
			// enchanted book with autosmelt would only be spawn-able.
			Enchantment.addToBookList(autosmelt);
		}

		// Initializes Stepboost enchantment if the enchantment ID isn't set 0
		if (Config.stepboostEnchantmentID != 0)
		{
			// Initializes the unbreakable variable
			stepboost = new EnchantmentStepboost(Config.stepboostEnchantmentID).setName(VTweaks.modid + "_stepboost");
			// Adds said enchantment to the Enchanted Books list. Otherwise an
			// enchanted book with unbreakable would only be spawn-able.
			Enchantment.addToBookList(stepboost);
		}
		
		if (Config.lumberingEnchantmentID != 0)
		{
			lumbering = new EnchantmentLumbering(Config.lumberingEnchantmentID).setName(VTweaks.modid + "_lumbering");
			
			Enchantment.addToBookList(lumbering);
		}

	}
}