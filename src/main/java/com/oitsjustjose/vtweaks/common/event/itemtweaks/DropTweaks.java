package com.oitsjustjose.vtweaks.common.event.itemtweaks;

import com.oitsjustjose.vtweaks.common.config.ItemTweakConfig;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DropTweaks {

    @SubscribeEvent
    public void registerTweak(ItemExpireEvent event) {
        if (event.getEntityItem() == null || event.getEntityItem().getItem().isEmpty()) {
            return;
        }

        if (!event.getEntityItem().world.isRemote) {
            return;
        }

        ItemEntity entItem = event.getEntityItem();
        World world = entItem.getEntityWorld();
        ItemStack stack = entItem.getItem();

        // Handles egg hatching; configurable chance.
        if (ItemTweakConfig.ENABLE_EGG_HATCHING.get() && stack.getItem() == Items.EGG) {
            if (world.getRandom().nextInt(100) <= ItemTweakConfig.EGG_HATCING_CHANCE.get()) {
                if (!world.isRemote) {
                    ChickenEntity chick = new ChickenEntity(EntityType.CHICKEN, world);
                    chick.setGrowingAge(-24000);
                    chick.setLocationAndAngles(entItem.getPosX(), entItem.getPosY(), entItem.getPosZ(),
                            entItem.rotationYaw, 0.0F);
                    world.addEntity(chick);
                }
            }
        }
        // Handles sapling replanting; 100% chance
        else if (ItemTweakConfig.ENABLE_SAPLING_SELF_PLANTING.get()
                && ItemTags.SAPLINGS.contains(stack.getItem())) {
            BlockPos saplingPos = fromDouble(entItem.getPosX(), entItem.getPosY(), entItem.getPosZ());
            // Checks to see if where the sapling *will* be is air
            if (world.isAirBlock(saplingPos) || world.getBlockState(saplingPos).getMaterial().isReplaceable()) {
                Item item = stack.getItem();
                if (item instanceof BlockItem) {
                    BlockItemUseContext context = new DirectionalPlaceContext(world, saplingPos, Direction.DOWN, stack,
                            Direction.UP);
                    ((BlockItem) item).tryPlace(context);
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