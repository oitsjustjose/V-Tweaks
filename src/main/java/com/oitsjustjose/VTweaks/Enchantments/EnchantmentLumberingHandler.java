package com.oitsjustjose.VTweaks.Enchantments;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class EnchantmentLumberingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BreakEvent event)
	{
		if(event.block == null || event.world == null || event.getPlayer() == null)
			return;	
		
		ItemStack block = new ItemStack(event.block, 1, event.blockMetadata);
		World world = event.world;
		EntityPlayer player = event.getPlayer();
		

	}
}
