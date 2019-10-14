package com.oitsjustjose.vtweaks.event.itemtweaks;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.oitsjustjose.vtweaks.config.ItemTweakConfig;
import com.oitsjustjose.vtweaks.util.Constants;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DropTweaks
{

    @SubscribeEvent
    public void registerTweak(ItemExpireEvent event)
    {
        if (event.getEntityItem() == null || event.getEntityItem().getItem().isEmpty())
        {
            return;
        }

        ItemEntity entItem = event.getEntityItem();
        World world = entItem.getEntityWorld();
        ItemStack stack = entItem.getItem();

        // Handles egg hatching; configurable chance.
        if (ItemTweakConfig.ENABLE_EGG_HATCHING.get() && stack.getItem() == Items.EGG)
        {
            if (world.getRandom().nextInt(100) <= ItemTweakConfig.EGG_HATCING_CHANCE.get())
            {
                if (!world.isRemote)
                {
                    ChickenEntity chick = new ChickenEntity(EntityType.CHICKEN, world);
                    chick.setGrowingAge(-24000);
                    chick.setLocationAndAngles(entItem.posX, entItem.posY, entItem.posZ, entItem.rotationYaw, 0.0F);
                    world.addEntity(chick);
                }
            }
        }
        // Handles sapling replanting; 100% chance
        else if (ItemTweakConfig.ENABLE_SAPLING_SELF_PLANTING.get()
                && Block.getBlockFromItem(stack.getItem()).getClass().getName().contains("BlockSapling"))
        {
            BlockPos saplingPos = fromDouble(entItem.posX, entItem.posY, entItem.posZ);
            // Checks to see if where the sapling *will* be is air
            if (world.isAirBlock(saplingPos))
            {
                FakePlayer fake = new FakePlayer(world.getServer().getWorld(world.getDimension().getType()),
                        new GameProfile(UUID.nameUUIDFromBytes(Constants.MODID.getBytes()), "VTweaksFake"));
                fake.setHeldItem(Hand.MAIN_HAND, stack);
                stack.onItemUse(new ItemUseContext(fake, Hand.MAIN_HAND,
                        new BlockRayTraceResult(new Vec3d(0D, 0D, 0D), Direction.UP, saplingPos, false)));
            }
        }
    }

    @SubscribeEvent
    public void registerTweak(ItemTossEvent event)
    {
        // Checks to see if the despawn time is -1. If it is, items won't despawn, so
        // nothing to do here.
        if (ItemTweakConfig.DESPAWN_TIME_OVERRIDE.get() == -1 || event.getEntityItem() == null)
        {
            return;
        }

        ItemEntity entItem = event.getEntityItem();
        entItem.lifespan = ItemTweakConfig.DESPAWN_TIME_OVERRIDE.get();
    }

    private BlockPos fromDouble(double xIn, double yIn, double zIn)
    {
        int x = (xIn % 1) >= .5 ? (int) Math.ceil(xIn) : (int) Math.floor(xIn);
        int y = (yIn % 1) >= .5 ? (int) Math.ceil(yIn) : (int) Math.floor(yIn);
        int z = (zIn % 1) >= .5 ? (int) Math.ceil(zIn) : (int) Math.floor(zIn);
        return new BlockPos(x, y, z);
    }
}