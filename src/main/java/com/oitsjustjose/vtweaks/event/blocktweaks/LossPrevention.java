package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Iterator;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LossPrevention
{
	@SubscribeEvent
	public void registerLavaTweak(HarvestDropsEvent event)
	{
		// Checks to see if feature is enabled
		if (!VTweaks.config.enableLavaLossPrevention)
			return;
		// Confirming that player exists
		if (event.getHarvester() == null || event.getState() == null || event.getState().getBlock() == null)
			return;

		EntityPlayer player = event.getHarvester();

		if (shouldPreventLoss(event.getState()) && dangerousPos(event.getWorld(), event.getPos(), new Block[] { Blocks.LAVA, Blocks.FLOWING_LAVA }))
		{
			Iterator<ItemStack> iter = event.getDrops().iterator();
			while (iter.hasNext())
			{
				ItemStack drop = iter.next().copy();
				if (!event.getWorld().isRemote)
				{
					if (!player.inventory.addItemStackToInventory(drop))
					{
						event.getWorld().spawnEntity(HelperFunctions.createItemEntity(event.getWorld(), player.getPosition(), drop));
					}
				}
				iter.remove();
			}
		}
	}

	/**
	 * @SubscribeEvent public void registerCactusTweak(EntityJoinWorldEvent event) { // Checks to see if feature is enabled if (!VTweaks.config.enableCactusLossPrevention) return; // Confirming that player exists if (event.getEntity() == null || !(event.getEntity() instanceof EntityItem)) return;
	 * 
	 *                 EntityItem entItem = (EntityItem) event.getEntity();
	 * 
	 *                 if (entItem.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.CACTUS)) entItem.setEntityInvulnerable(true); }
	 **/

	@SubscribeEvent
	public void registerCactusTweak(ItemEvent event)
	{
		if (!VTweaks.config.enableCactusLossPrevention)
			return;

		if (event.getEntityItem() == null || event.getEntityItem().getEntityItem().isEmpty())
			return;

		if (event.getEntityItem().getEntityItem().getItem() == Item.getItemFromBlock(Blocks.CACTUS))
		{
			final double x = event.getEntity().posX;
			final double y = event.getEntity().posY;
			final double z = event.getEntity().posZ;
			EntityItem cactus = new EntityItem(event.getEntityItem().getEntityWorld(), x, y, z, event.getEntityItem().getEntityItem())
			{
				@Override
				public boolean attackEntityFrom(DamageSource source, float amount)
				{
					if (source == DamageSource.CACTUS)
					{
						this.setPositionAndUpdate(x, y, z);
						return false;
					}
					return super.attackEntityFrom(source, amount);
				}
			};

			cactus.setDefaultPickupDelay();
			cactus.setVelocity(event.getEntityItem().motionX, event.getEntityItem().motionY, event.getEntityItem().motionZ);
			if (event.isCancelable())
				event.setCanceled(true);
			if (!event.getEntityItem().getEntityWorld().isRemote)
				event.getEntityItem().getEntityWorld().spawnEntity(cactus);
			event.getEntityItem().setDead();
		}

	}

	private boolean dangerousPos(World world, BlockPos pos, Block... blocks)
	{
		for (Block b : blocks)
			if (world.getBlockState(pos.down()).getBlock() == b)
				return true;
		return false;
	}

	private boolean shouldPreventLoss(IBlockState state)
	{
		ItemStack compare = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
		for (ItemStack i : VTweaks.config.lavaLossBlockList)
			if (i.getItem() == compare.getItem() && i.getMetadata() == compare.getMetadata())
				return true;
		return state.getBlock() == Blocks.CACTUS;
	}
}
