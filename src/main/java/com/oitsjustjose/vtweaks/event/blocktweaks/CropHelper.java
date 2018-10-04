package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Iterator;

import com.oitsjustjose.vtweaks.util.HelperFunctions;
import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CropHelper
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerVanilla(RightClickBlock event)
    {
        // Checks if feature is enabled
        if (!ModConfig.blockTweaks.cropTweak.enableCropTweak)
        {
            return;
        }

        event.getWorld().getBlockState(event.getPos());
        if (event.getEntityPlayer() == null || event.getHand() != EnumHand.MAIN_HAND)
        {
            return;
        }

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block harvestable = state.getBlock();

        for (String blackList : ModConfig.blockTweaks.cropTweak.blacklist)
        {
            if (harvestable.getClass().getName().toLowerCase().contains(blackList.toLowerCase()))
            {
                return;
            }
        }

        if (harvestable instanceof BlockCrops)
        {
            if (harvestGenericCrop(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
            {
                event.setCanceled(true);
            }
        }
        else if (harvestable instanceof BlockNetherWart)
        {
            if (harvestNetherWart(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
            {
                event.setCanceled(true);
            }
        }
        else if (harvestable instanceof BlockCocoa)
        {
            if (harvestCocoaPod(event.getWorld(), event.getPos(), state, event.getEntityPlayer()))
            {
                event.setCanceled(true);
            }
        }
    }

    public boolean harvestGenericCrop(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockCrops crop = (BlockCrops) state.getBlock();
        if (crop.isMaxAge(state))
        {
            if (!world.isRemote)
            {
                final NonNullList<ItemStack> drops = NonNullList.create();
                final Item mainDrop = crop.getItemDropped(state, world.rand, 0);
                crop.getDrops(drops, world, pos, state, 0);
                // An iterator to remove a seed or potato / carrot
                Iterator<ItemStack> iter = drops.iterator();
                while (iter.hasNext())
                {
                    final ItemStack next = iter.next();
                    if (next.getItem() != mainDrop || next.getItem() == Items.POTATO || next.getItem() == Items.CARROT)
                    {
                        iter.remove();
                        break;
                    }
                }
                for (ItemStack i : drops)
                {
                    dropItem(world, player.getPosition(), i);
                }
                world.setBlockState(pos, crop.withAge(0));
            }
            player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    private boolean harvestNetherWart(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockNetherWart wart = (BlockNetherWart) state.getBlock();
        final NonNullList<ItemStack> drops = NonNullList.create();
        wart.getDrops(drops, world, pos, state, 0);
        // This is how I'm determining the crop is grown
        if (drops.size() > 1)
        {
            if (!world.isRemote)
            {
                drops.remove(0);
                for (ItemStack i : drops)
                {
                    dropItem(world, player.getPosition(), i);
                }
                world.setBlockState(pos, wart.getDefaultState());
            }
            player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    private boolean harvestCocoaPod(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        BlockCocoa cocoa = (BlockCocoa) state.getBlock();
        final NonNullList<ItemStack> drops = NonNullList.create();
        cocoa.getDrops(drops, world, pos, state, 0);
        // This is how I'm determining the crop is grown
        if (drops.size() > 1)
        {
            if (!world.isRemote)
            {
                drops.remove(0);
                for (ItemStack i : drops)
                {
                    dropItem(world, player.getPosition(), i);
                }
                world.setBlockState(pos, state.withProperty(BlockCocoa.AGE, Integer.valueOf(0)));
            }
            player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }

    private void dropItem(World world, BlockPos pos, ItemStack itemstack)
    {
        world.spawnEntity(HelperFunctions.createItemEntity(world, pos, itemstack));
    }
}
