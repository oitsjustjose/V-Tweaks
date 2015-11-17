package com.oitsjustjose.VTweaks.Tweaks;

import com.oitsjustjose.VTweaks.Util.ConfigHandler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ClientTweaks
{
	/*
	 * An entire class for two vanilla tweaks which are really neat!
	 */

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerEvent(PlayerEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;
		ItemStack item = player.getHeldItem();
		if (item == null)
			return;
		while (EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item) > 0)
		{
			while (player.openContainer == null)
				world.spawnParticle("lava", (double) player.posX, (double) player.posY + 1, (double) player.posZ, 0, 0, 0);
		}
	}

}