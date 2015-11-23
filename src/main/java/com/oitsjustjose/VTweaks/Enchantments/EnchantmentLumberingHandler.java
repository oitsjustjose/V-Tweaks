package com.oitsjustjose.VTweaks.Enchantments;

import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class EnchantmentLumberingHandler
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void register(BreakEvent event)
	{
		if (event.block == null || event.world == null || event.getPlayer() == null
				|| event.getPlayer().getHeldItem() == null)
			return;

		ItemStack block = new ItemStack(event.block, 1, event.blockMetadata);
		World world = event.world;
		EntityPlayer player = event.getPlayer();
		ItemStack axe = player.getHeldItem();

		if (EnchantmentHelper.getEnchantmentLevel(Config.lumberingEnchantmentID, axe) != 0)
		{
			if (event.block.isWood(world, event.x, event.y, event.z))
			{
//				event.setCanceled(true);
				event.block.breakBlock(world, event.x, event.y, event.z, world.getBlock(event.x, event.y, event.z), world.getBlockMetadata(event.x, event.y, event.z));
				for (int x = 0; x < 16; x++)
					for (int y = 0; y < 48; y++)
						for (int z = 0; z < 16; z++)
							if (event.block.isWood(world, x, y, z))
							{
								event.block.breakBlock(world, x, y, z, world.getBlock(event.x, event.y, event.z), world.getBlockMetadata(event.x, event.y, event.z));
							}
			}
		}
	}
}
