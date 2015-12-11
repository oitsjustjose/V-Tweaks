package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

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
	
	public static boolean hasAutoSmelt(ItemStack itemstack)
	{
		if(Config.autosmeltEnchantmentID == 0)
			return false;
		
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Config.autosmeltEnchantmentID, itemstack);
		
		if(autosmeltLevel > 0)
			return true;
		
		return false;
	}
}