package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class BonemealTweaks {
    @SubscribeEvent
    public void registerTweak(RightClickBlock event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_BONEMEAL_TWEAK.get() || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Level world = event.getWorld();
        Block testFor = world.getBlockState(event.getPos()).getBlock();
        Random rand = world.random;

        if (!player.getMainHandItem().isEmpty()) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() == Items.BONE_MEAL) {
                if (testFor == Blocks.CACTUS || testFor == Blocks.SUGAR_CANE) {
                    if (world.getBlockState(event.getPos().below(2)).getBlock() != testFor
                            && world.getBlockState(event.getPos().above()).isAir()) {
                        player.swing(InteractionHand.MAIN_HAND);
                        for (int i = 0; i < 8; i++) {
                            spawnParticles(world, rand, event.getPos());
                        }
                        if (!world.isClientSide()) {
                            if (!player.isCreative()) {
                                heldItem.setCount(heldItem.getCount() - 1);
                            }
                            world.setBlock(event.getPos().above(), world.getBlockState(event.getPos()), 2);
                        }
                    }
                }
            }
        }
    }

    private void spawnParticles(Level world, Random rand, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        double d0 = rand.nextGaussian() * 0.02D;
        double d1 = rand.nextGaussian() * 0.02D;
        double d2 = rand.nextGaussian() * 0.02D;
        world.addParticle(ParticleTypes.HAPPY_VILLAGER, (float) pos.getX() + rand.nextFloat(),
                (double) pos.getY() + (double) rand.nextFloat() * state.getShape(world, pos).max(Direction.Axis.Y),
                (float) pos.getZ() + rand.nextFloat(), d0, d1, d2);
    }
}