package com.oitsjustjose.VTweaks.Enchantments;

import net.minecraft.enchantment.Enchantment;

import com.oitsjustjose.VTweaks.VTweaks;
import com.oitsjustjose.VTweaks.Util.ConfigHandler;

public class Enchantments
{
	public static Enchantment unbreakable;
	public static Enchantment autosmelt;

	public static void initialize()
	{
		// Initializes unbreakable if the enchantment ID isn't set to 0
		if (ConfigHandler.unbreakableEnchantmentID != 0)
		{
			// Initializes the unbreakable variable
			unbreakable = new EnchantmentUnbreakable(ConfigHandler.unbreakableEnchantmentID)
					.setName(VTweaks.modid + "_unbreakable");
			// Adds said enchantment to the Enchanted Books list. Otherwise an
			// enchanted book with unbreakable would only be spawn-able.
			Enchantment.addToBookList(unbreakable);
		}

		// Initializes autosmelt if the enchantment ID isn't set to 0
		if (ConfigHandler.autosmeltEnchantmentID != 0)
		{
			// Initializes the autosmelt variable
			autosmelt = new EnchantmentAutosmelt(ConfigHandler.autosmeltEnchantmentID)
					.setName(VTweaks.modid + "_autosmelt");
			// Adds said enchantment to the Enchanted Books list. Otherwise an
			// enchanted book with autosmelt would only be spawn-able.
			Enchantment.addToBookList(autosmelt);
		}
	}
}