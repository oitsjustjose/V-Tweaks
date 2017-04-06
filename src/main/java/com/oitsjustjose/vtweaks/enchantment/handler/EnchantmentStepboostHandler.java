package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentStepboostHandler
{
	@SubscribeEvent
	public void register(PlayerEvent event)
	{
		// Check if enchantment is disabled
		if (VTweaks.config.stepboostID <= 0)
			return;
		// Local Variables
		EntityPlayer player = event.getEntityPlayer();
		NBTTagCompound persistTag = getPlayerPersistTag(player, VTweaks.MODID);
		ItemStack boots = player.inventory.armorInventory.get(0);
		int EnchantmentLevelArmor = EnchantmentHelper.getEnchantmentLevel(Enchantments.stepboost, boots);
		final String VTWEAKS_STEP_BOOST = "VTweaksStepBoost";
		final String JUMPBOOST_SETTING = "VTweaksJumpboostSetting";

		// Boots are ON and have the enchantment
		if (!boots.isEmpty() && EnchantmentLevelArmor != 0)
		{
			persistTag.setBoolean(VTWEAKS_STEP_BOOST, true);
			persistTag.setBoolean(JUMPBOOST_SETTING, Minecraft.getMinecraft().gameSettings.autoJump);
			Minecraft.getMinecraft().gameSettings.setOptionValue(Options.AUTO_JUMP, 0);
			if (player.stepHeight < 1.0F && !player.isSneaking())
				player.stepHeight += 0.5F;
			if (player.stepHeight >= 1.0F && player.isSneaking())
				player.stepHeight -= 0.5F;
		}
		// No boots OR no enchantment, checks if the tag thought you did at one point have it
		else if (persistTag.getBoolean(VTWEAKS_STEP_BOOST))
		{
			persistTag.setBoolean(VTWEAKS_STEP_BOOST, false);
			Minecraft.getMinecraft().gameSettings.setOptionValue(Options.AUTO_JUMP, Boolean.valueOf(persistTag.getBoolean(JUMPBOOST_SETTING)).compareTo(false));
			player.stepHeight -= 0.5F;
		}
	}

	private NBTTagCompound getPlayerPersistTag(EntityPlayer player, String modid)
	{
		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound persistTag = null;

		if (tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
		{
			persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}
		else
		{
			persistTag = new NBTTagCompound();
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
		}

		NBTTagCompound modTag = null;
		if (persistTag.hasKey(modid))
		{
			modTag = persistTag.getCompoundTag(modid);
		}
		else
		{
			modTag = new NBTTagCompound();
			persistTag.setTag(modid, modTag);
		}

		return modTag;
	}
}