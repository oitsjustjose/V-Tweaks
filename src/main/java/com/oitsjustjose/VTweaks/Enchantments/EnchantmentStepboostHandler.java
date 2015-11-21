package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EnchantmentStepboostHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerTweak(PlayerEvent event)
	{
		// Checks the boot itemstack
		ItemStack boots = event.entityPlayer.getCurrentArmor(0);
		// Gets the Step Boost enchantment level on your boots
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Config.stepboostEnchantmentID, boots);
		// Does the thing!
		if (event.entityPlayer.worldObj.isRemote)
			return;
		if (boots != null && EnchantmentLevelArmor > 0)
			event.entityPlayer.stepHeight = 1.0F;
		else
			event.entityPlayer.stepHeight = 0.5F;
	}
}