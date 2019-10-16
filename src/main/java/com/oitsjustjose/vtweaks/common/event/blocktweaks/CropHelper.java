package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import java.util.List;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
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

        if (harvestable instanceof CropsBlock)
        {
            if (harvestGenericCrop(event.getWorld(), event.getPos(), state, player))
            {
                event.setCanceled(true);
            }
        }
        else if (harvestable instanceof NetherWartBlock)
        {
            if (harvestNetherWart(event.getWorld(), event.getPos(), state, player))
            {
                event.setCanceled(true);
            }
        }
        else if (harvestable instanceof CocoaBlock)
        {
            if (harvestCocoaPod(event.getWorld(), event.getPos(), state, player))
            {
                event.setCanceled(true);
            }
        }

        if (event.isCanceled())
        {
            player.swingArm(Hand.MAIN_HAND);
            event.getWorld().playSound(player, event.getPos(), SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public boolean harvestGenericCrop(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        CropsBlock crop = (CropsBlock) state.getBlock();
        if (crop.isMaxAge(state))
        {
            if (!world.isRemote)
            {
                List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);

                drops.forEach((stack) -> {
                    if (stack.getCount() > 1)
                    {
                        stack.shrink(1);
                    }
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                });
            }
            world.playEvent(player, 2001, pos, Block.getStateId(state));
            world.setBlockState(pos, crop.withAge(0));
            return true;
        }
        return false;
    }

    private boolean harvestNetherWart(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if (state.get(NetherWartBlock.AGE) >= 3)
        {
            if (!world.isRemote)
            {
                List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);

                drops.forEach((stack) -> {
                    if (stack.getCount() > 1)
                    {
                        stack.shrink(1);
                    }
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                });
            }
            world.playEvent(player, 2001, pos, Block.getStateId(state));
            world.setBlockState(pos, state.with(NetherWartBlock.AGE, 0));
            return true;
        }
        return false;
    }

    private boolean harvestCocoaPod(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        if (state.get(CocoaBlock.AGE) >= 2)
        {
            if (!world.isRemote)
            {
                List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);

                drops.forEach((stack) -> {
                    if (stack.getCount() > 1)
                    {
                        stack.shrink(1);
                    }
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                });
            }
            world.playEvent(player, 2001, pos, Block.getStateId(state));
            world.setBlockState(pos, state.with(CocoaBlock.AGE, 0));
            return true;
        }
        return false;
    }
}
