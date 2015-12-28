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
		if (Config.hypermendingID != 0)
		{
			hyperMending = new EnchantmentHypermending(Config.hypermendingID).setName(VTweaks.modid + "_hyperMending");
			Enchantment.addToBookList(hyperMending);
		}

		if (Config.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt(Config.autosmeltID).setName(VTweaks.modid + "_autosmelt");
			Enchantment.addToBookList(autosmelt);
		}

		if (Config.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost(Config.stepboostID).setName(VTweaks.modid + "_stepboost");
			Enchantment.addToBookList(stepboost);
		}

		if (Config.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering(Config.lumberingID).setName(VTweaks.modid + "_lumbering");
			Enchantment.addToBookList(lumbering);
		}

	}
	
	public static boolean hasAutoSmelt(ItemStack itemstack)
	{
		if(Config.autosmeltID == 0 || itemstack == null)
			return false;
		
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Config.autosmeltID, itemstack);
		
		if(autosmeltLevel > 0)
			return true;
		
		return false;
	}
}