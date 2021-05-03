/**
 * Ported from https://github.com/ternsip/ChopDown/
 * 
 * Lots of code here is from there but in my own interpreted way
 */

package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChopDown {
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (!BlockTweakConfig.ENABLE_TREE_CHOP_DOWN.get()) {
            return;
        }

        if (!(evt.getWorld() instanceof World)) {
            return;
        }

        int lumberLvl = EnchantmentHelper.getEnchantmentLevel(VTweaks.lumbering, evt.getPlayer().getHeldItemMainhand());

        // Drop early if lumbering..
        if (evt.getPlayer().isSneaking() && lumberLvl > 0) {
            return;
        }

        World world = (World) evt.getWorld();
        BlockPos pos = evt.getPos();

        for (int dy = 0; dy < BlockTweakConfig.TREE_CHOP_DOWN_LOG_COUNT.get(); dy++) {
            if (!BlockTags.LOGS.contains(world.getBlockState(pos.add(0, dy, 0)).getBlock())) {
                return;
            }
        }

        int rad = 32;
        int leaf = 5;
        int dirX = Math.max(-1, Math.min(1, pos.getX() - (int) Math.round(evt.getPlayer().getPosX() - 0.5)));
        int dirZ = Math.max(-1, Math.min(1, pos.getZ() - (int) Math.round(evt.getPlayer().getPosZ() - 0.5)));
        // Let's not chop down everything if it's basically not a tree
        boolean leavesFound = false;

        LinkedList<BlockPos> queue = new LinkedList<BlockPos>();
        HashMap<BlockPos, Integer> used = new HashMap<BlockPos, Integer>();

        queue.add(pos);
        used.put(pos, leaf);

        while (!queue.isEmpty()) {
            BlockPos top = queue.pollFirst();

            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        BlockPos tmpPos = top.add(dx, dy, dz);
                        int step = used.get(top);
                        if (step <= 0 || tmpPos.distanceSq(pos) > (rad * rad)) {
                            continue;
                        }

                        BlockState bs = world.getBlockState(tmpPos);
                        boolean isLog = BlockTags.LOGS.contains(bs.getBlock());
                        boolean isLeaf = BlockTags.LEAVES.contains(bs.getBlock());

                        if (isLeaf && !leavesFound) {
                            leavesFound = true;
                        }
                        // Check if there's a log directly above

                        if ((dy >= 0 && step == leaf && isLog) || isLeaf) {
                            step -= (isLeaf ? 1 : 0);
                            if (!used.containsKey(tmpPos) || used.get(tmpPos) < step) {
                                used.put(tmpPos, step);
                                queue.push(tmpPos);
                            }
                        }
                    }
                }
            }
        }

        if (leavesFound) {
            // Start moving things now that we've calculated it all
            for (Entry<BlockPos, Integer> entry : used.entrySet()) {
                BlockPos blockPos = entry.getKey();
                if (!pos.equals(blockPos) && canBeMoved(world, blockPos.add(0, -1, 0))) {
                    int oy = blockPos.getY() - pos.getY();
                    moveAsBlockEntity(world, blockPos, blockPos.add(oy * dirX, 0, oy * dirZ));
                }
            }
        }
    }

    private boolean canBeMoved(World world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Block b = bs.getBlock();

        return !bs.hasTileEntity() && (BlockTags.LOGS.contains(b) || BlockTags.LEAVES.contains(b)
                || BlockTags.BEEHIVES.contains(b) || b.isAir(bs, world, pos));
    }

    private void moveAsBlockEntity(World world, BlockPos pos, BlockPos newPos) {
        BlockState bs = world.getBlockState(pos);
        // destroy some leaves
        if (BlockTags.LEAVES.contains(bs.getBlock()) && world.rand.nextBoolean()) {
            Block.spawnDrops(bs, world, newPos, null);
        } else {
            CustomFallingBlockEntity e = new CustomFallingBlockEntity(world, newPos, bs);
            world.addEntity(e);
        }
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

    class CustomFallingBlockEntity extends FallingBlockEntity {
        public CustomFallingBlockEntity(World world, BlockPos pos, BlockState s) {
            // Adjust by 0.5 to snap to block axis
            super(world, pos.getX() + (pos.getX() < 0 ? 0.5 : -0.5), pos.getY(),
                    pos.getZ() + (pos.getZ() < 0 ? 0.5 : -0.5), s);
            this.setBoundingBox(new AxisAlignedBB(pos.add(0, 0, 0), pos.add(1, 1, 1)));
            this.fallTime = 1;
        }

        @Nullable
        @Override
        public ItemEntity entityDropItem(ItemStack stack, float offsetY) {
            BlockState state = this.getBlockState();

            if (state != null && state.getBlock() instanceof LeavesBlock) {
                Block.spawnDrops(state, world, this.getPosition(), null);
                return null;
            }

            return super.entityDropItem(stack, offsetY);
        }
    }
}
