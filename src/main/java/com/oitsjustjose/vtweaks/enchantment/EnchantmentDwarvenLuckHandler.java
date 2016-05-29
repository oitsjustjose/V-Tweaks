package com.oitsjustjose.vtweaks.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentDwarvenLuckHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BlockEvent.HarvestDropsEvent event)
	{
		EntityPlayer player = event.getHarvester();
		if (player == null || player.getHeldItemMainhand() == null)
			return;

		ItemStack heldItem = player.getHeldItemMainhand();
		int dwarvenLuckLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.dwarvenLuck, heldItem);

		if (dwarvenLuckLevel > 0 && player.getRNG().nextInt(192) == 0 && player.getPosition().getY() < 32)
			event.getDrops().add(new ItemStack(Items.GOLD_NUGGET, (player.getRNG().nextInt(event.getFortuneLevel() + 1) + 1)));
	}
}