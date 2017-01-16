package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.List;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LavaLossPrevention
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void registerTweak(HarvestDropsEvent event)
	{
		if(!VTweaks.config.enableLavaLossPrevention)
			return;
		
		EntityPlayer player = event.getHarvester();
		if (player == null || event.getState() == null || event.getState().getBlock() == null)
			return;

		Block chiselBasalt = Block.REGISTRY.getObject(new ResourceLocation("chisel", "basaltextra"));
		Block blockBroken = event.getState().getBlock();

		if (blockBroken == Blocks.OBSIDIAN || (blockBroken == chiselBasalt && chiselBasalt.getMetaFromState(event.getState()) == 7))
		{
			if (isAboveLava(event.getWorld(), event.getPos()))
			{

				List<ItemStack> list = event.getDrops();
				final ItemStack drop = list.get(0);
				list.clear();

				if (!event.getWorld().isRemote)
				{
					if (!player.inventory.addItemStackToInventory(drop))
					{
						event.getWorld().spawnEntity(new EntityItem(event.getWorld(), player.posX, player.posY, player.posZ, drop));
					}
				}
			}
		}
	}

	boolean isAboveLava(World world, BlockPos pos)
	{
		if (world.getBlockState(pos.down()).getBlock() == Blocks.LAVA || world.getBlockState(pos.down()).getBlock() == Blocks.FLOWING_LAVA)
			return true;
		return false;
	}
}
