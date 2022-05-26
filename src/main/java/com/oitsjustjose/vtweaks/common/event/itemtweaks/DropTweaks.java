package com.oitsjustjose.vtweaks.common.event.itemtweaks;

import com.oitsjustjose.vtweaks.common.config.ItemTweakConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DropTweaks {

    @SubscribeEvent
    public void registerTweak(ItemExpireEvent event) {
        if (event.getEntityItem() == null || event.getEntityItem().getItem().isEmpty()) {
            return;
        }

        if (!event.getEntityItem().level.isClientSide) {
            return;
        }

        ItemEntity entItem = event.getEntityItem();
        Level world = entItem.level;
        ItemStack stack = entItem.getItem();

        // Handles sapling replanting; 100% chance
        if (ItemTweakConfig.ENABLE_SAPLING_SELF_PLANTING.get() && stack.is(ItemTags.SAPLINGS)) {
            BlockPos saplingPos = fromDouble(entItem.getX(), entItem.getY(), entItem.getZ());
            // Checks to see if where the sapling *will* be is air
            if (world.getBlockState(saplingPos).isAir()
                    || world.getBlockState(saplingPos).getMaterial().isReplaceable()) {
                Item item = stack.getItem();
                if (item instanceof BlockItem) {
                    BlockPlaceContext context = new DirectionalPlaceContext(world, saplingPos, Direction.DOWN, stack,
                            Direction.UP);
                    ((BlockItem) item).place(context);
                }
            }
        }
        // Handles items that are supposed to never despawn
        else if (ItemTweakConfig.DESPAWN_TIME_OVERRIDE.get() == -1) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void registerTweak(ItemTossEvent event) {
        // Checks to see if the despawn time is -1. If it is, items won't despawn, so
        // nothing to do here.
        if (ItemTweakConfig.DESPAWN_TIME_OVERRIDE.get() == -1 || !ItemTweakConfig.ENABLE_DESPAWN_TIME_OVERRIDE.get()
                || event.getEntityItem() == null) {
            return;
        }

        ItemEntity entItem = event.getEntityItem();
        entItem.lifespan = ItemTweakConfig.DESPAWN_TIME_OVERRIDE.get();
    }

    private BlockPos fromDouble(double xIn, double yIn, double zIn) {
        int x = (xIn % 1) >= .5 ? (int) Math.ceil(xIn) : (int) Math.floor(xIn);
        int y = (yIn % 1) >= .5 ? (int) Math.ceil(yIn) : (int) Math.floor(yIn);
        int z = (zIn % 1) >= .5 ? (int) Math.ceil(zIn) : (int) Math.floor(zIn);
        return new BlockPos(x, y, z);
    }
}