package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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
			hyperMending = new EnchantmentHypermending().setName(VTweaks.MODID + ":hypermending");
			Enchantment.REGISTRY.register(VTweaks.modConfig.hypermendingID, new ResourceLocation(VTweaks.MODID, "hypermending"), hyperMending);
		}

		if (VTweaks.modConfig.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt().setName(VTweaks.MODID + ":autosmelt");
			Enchantment.REGISTRY.register(VTweaks.modConfig.autosmeltID, new ResourceLocation(VTweaks.MODID, "autosmelt"), autosmelt);
		}

		if (VTweaks.modConfig.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost().setName(VTweaks.MODID + ":stepboost");
			Enchantment.REGISTRY.register(VTweaks.modConfig.stepboostID, new ResourceLocation(VTweaks.MODID, "stepboost"), stepboost);
		}

		if (VTweaks.modConfig.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering().setName(VTweaks.MODID + ":lumbering");
			Enchantment.REGISTRY.register(VTweaks.modConfig.lumberingID, new ResourceLocation(VTweaks.MODID, "lumbering"), lumbering);
		}

	}

	public static boolean hasAutoSmelt(ItemStack itemstack)
	{
		if (VTweaks.modConfig.autosmeltID == 0 || itemstack == null)
			return false;

		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(autosmelt, itemstack);

		if (autosmeltLevel > 0)
			return true;

		return false;
	}
}