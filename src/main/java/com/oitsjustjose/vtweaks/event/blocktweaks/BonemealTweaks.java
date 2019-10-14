package com.oitsjustjose.vtweaks.event.blocktweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BonemealTweaks
{
    @SubscribeEvent
    public void registerTweak(RightClickBlock event)
    {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_BONEMEAL_TWEAK.get() || !(event.getEntity() instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntity();
        World world = event.getWorld();
        Block testFor = world.getBlockState(event.getPos()).getBlock();
        Random rand = world.rand;

        if (!player.getHeldItemMainhand().isEmpty())
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if (heldItem.getItem() == Items.BONE_MEAL)
            {
                if (testFor == Blocks.CACTUS || testFor == Blocks.SUGAR_CANE)
                {
                    if (world.getBlockState(event.getPos().down(2)).getBlock() != testFor
                            && world.isAirBlock(event.getPos().up()))
                    {
                        player.swingArm(Hand.MAIN_HAND);
                        for (int i = 0; i < 8; i++)
                        {
                            spawnParticles(world, rand, testFor, event.getPos());
                        }
                        if (!world.isRemote)
                        {
                            if (!player.isCreative())
                            {
                                heldItem.setCount(heldItem.getCount() - 1);
                            }
                            world.setBlockState(event.getPos().up(), world.getBlockState(event.getPos()), 2);
                        }
                    }
                }
            }
        }
    }

    private void spawnParticles(World world, Random rand, Block testFor, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        double d0 = rand.nextGaussian() * 0.02D;
        double d1 = rand.nextGaussian() * 0.02D;
        double d2 = rand.nextGaussian() * 0.02D;
        world.addParticle(ParticleTypes.HAPPY_VILLAGER, (double) ((float) pos.getX() + rand.nextFloat()),
                (double) pos.getY() + (double) rand.nextFloat() * state.getShape(world, pos).getEnd(Axis.Y),
                (double) ((float) pos.getZ() + rand.nextFloat()), d0, d1, d2);
    }
}