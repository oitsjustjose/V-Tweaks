package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import java.util.List;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import com.oitsjustjose.vtweaks.common.util.HelperFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class CropHelper
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerVanilla(RightClickBlock event)
    {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_CROP_TWEAK.get())
        {
            return;
        }

        event.getWorld().getBlockState(event.getPos());
        if (event.getEntity() == null || !(event.getEntity() instanceof PlayerEntity)
                || event.getHand() != Hand.MAIN_HAND)
        {
            return;
        }

        BlockState state = event.getWorld().getBlockState(event.getPos());
        PlayerEntity player = (PlayerEntity) event.getEntity();
        Block harvestable = state.getBlock();

        for (String blackList : BlockTweakConfig.CROP_TWEAK_BLACKLIST.get())
        {
            if (ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blackList)) == harvestable)
            {
                return;
            }
        }

        if (event.getWorld() instanceof ServerWorld)
        {
            ServerWorld world = (ServerWorld) event.getWorld();

            if (harvestable instanceof CropsBlock)
            {
                if (harvestGenericCrop(world, event.getPos(), state, player))
                {
                    event.setCanceled(true);
                }
            }
            else if (harvestable instanceof NetherWartBlock)
            {
                if (harvestNetherWart(world, event.getPos(), state, player))
                {
                    event.setCanceled(true);
                }
            }
            else if (harvestable instanceof CocoaBlock)
            {
                if (harvestCocoaPod(world, event.getPos(), state, player))
                {
                    event.setCanceled(true);
                }
            }
        }

    }

    public boolean harvestGenericCrop(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        CropsBlock crop = (CropsBlock) state.getBlock();
        if (crop.isMaxAge(state))
        {
            if (!world.isRemote())
            {
                List<ItemStack> drops = Block.getDrops(state, world, pos, null);

                drops.forEach((stack) -> {
                    if (stack.getCount() > 1)
                    {
                        stack.shrink(1);
                    }
                    dropItem(world, player.getPosition(), stack);
                });

                world.setBlockState(pos, crop.withAge(0));
            }
            return true;
        }
        return false;
    }

    private boolean harvestNetherWart(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        NetherWartBlock wart = (NetherWartBlock) state.getBlock();
        List<ItemStack> drops = Block.getDrops(state, world, pos, null);
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
            return true;
        }
        return false;
    }

    private boolean harvestCocoaPod(ServerWorld world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        List<ItemStack> drops = Block.getDrops(state, world, pos, null);
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
                world.setBlockState(pos, state.with(CocoaBlock.AGE, Integer.valueOf(0)));
            }
            return true;
        }
        return false;
    }

    private void dropItem(World world, BlockPos pos, ItemStack itemstack)
    {
        world.addEntity(HelperFunctions.createItemEntity(world, pos, itemstack));
    }
}
