package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

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
		if (VTweaks.modConfig.hypermendingID != 0)
		{
			hyperMending = new EnchantmentHypermending(VTweaks.modConfig.hypermendingID).setName(VTweaks.MODID + "_hyperMending");
			Enchantment.addToBookList(hyperMending);
		}

		if (VTweaks.modConfig.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt(VTweaks.modConfig.autosmeltID).setName(VTweaks.MODID + "_autosmelt");
			Enchantment.addToBookList(autosmelt);
		}

		if (VTweaks.modConfig.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost(VTweaks.modConfig.stepboostID).setName(VTweaks.MODID + "_stepboost");
			Enchantment.addToBookList(stepboost);
		}

		if (VTweaks.modConfig.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering(VTweaks.modConfig.lumberingID).setName(VTweaks.MODID + "_lumbering");
			Enchantment.addToBookList(lumbering);
		}

	}
	
	public static boolean hasAutoSmelt(ItemStack itemstack)
	{
		if(VTweaks.modConfig.autosmeltID == 0 || itemstack == null)
			return false;
		
		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(VTweaks.modConfig.autosmeltID, itemstack);
		
		if(autosmeltLevel > 0)
			return true;
		
		return false;
	}
}