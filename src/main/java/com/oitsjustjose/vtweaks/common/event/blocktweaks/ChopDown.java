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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChopDown {
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (!BlockTweakConfig.ENABLE_TREE_CHOP_DOWN.get()) {
            return;
        }

        int lumberLvl = EnchantmentHelper.getItemEnchantmentLevel(VTweaks.lumbering, evt.getPlayer().getMainHandItem());

        // Drop early if lumbering..
        if (evt.getPlayer().isCrouching() && lumberLvl > 0) {
            return;
        }

        if (!(evt.getWorld() instanceof Level)) {
            return;
        }

        BlockPos initialPos = evt.getPos();
        Level level = (Level) evt.getWorld();
        Player player = evt.getPlayer();

        for (int dy = 0; dy < BlockTweakConfig.TREE_CHOP_DOWN_LOG_COUNT.get(); dy++) {
            if (!BlockTags.LOGS.contains(level.getBlockState(initialPos.offset(0, dy, 0)).getBlock())) {
                return;
            }
        }

        int rad = 32;
        int leaf = 5;
        Direction dir = getDirection(player.blockPosition(), initialPos);

        // Let's not chop down everything if it's basically not a tree
        boolean leavesFound = false;

        LinkedList<BlockPos> queue = new LinkedList<BlockPos>();
        HashMap<BlockPos, Integer> used = new HashMap<BlockPos, Integer>();

        queue.add(initialPos);
        used.put(initialPos, leaf);

        while (!queue.isEmpty()) {
            BlockPos top = queue.pollFirst();

            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        BlockPos tmpPos = top.offset(dx, dy, dz);
                        int step = used.get(top);
                        if (step <= 0 || tmpPos.distSqr(initialPos) > (rad * rad)) {
                            continue;
                        }

                        BlockState bs = level.getBlockState(tmpPos);
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
                if (!initialPos.equals(blockPos) && canBeMoved(level, blockPos.offset(0, -1, 0))) {
                    BlockPos newPos = getNewPosition(initialPos, blockPos, dir);
                    moveAsBlockEntity(level, blockPos, newPos);
                }
            }
        }
    }

    private Direction getDirection(BlockPos playerPos, BlockPos brokenPos) {
        double x = (brokenPos.getX() + 0.5) - playerPos.getX();
        double z = (brokenPos.getZ() + 0.5) - playerPos.getZ();
        double abX = Math.abs(x);
        double abZ = Math.abs(z);
        int dirX = abX > abZ ? (int) Math.floor(abX / x) : 0;
        int dirZ = abX > abZ ? 0 : (int) Math.floor(abZ / z);
        if (dirX != 0) {
            return dirX == 1 ? Direction.EAST : Direction.WEST;
        }
        return dirZ == 1 ? Direction.SOUTH : Direction.NORTH;
    }

    private BlockPos getNewPosition(BlockPos initialBreakPos, BlockPos pos, Direction facing) {
        // Transforms height of tree into length of throw
        int transformedY = pos.getY() - initialBreakPos.getY();
        switch (facing) {
            case SOUTH:
                return pos.offset(0, 0, 1 + transformedY);
            case NORTH:
                return pos.offset(0, 0, 1 - transformedY);
            case EAST:
                return pos.offset(transformedY, 0, 1);
            case WEST:
                return pos.offset(-transformedY, 0, 1);
            default: // Up and Down won't be involved.
                return pos;
        }
    }

    private boolean canBeMoved(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        Block b = bs.getBlock();

        return !bs.hasBlockEntity() && (BlockTags.LOGS.contains(b) || BlockTags.LEAVES.contains(b)
                || BlockTags.BEEHIVES.contains(b) || bs.isAir());
    }

    private void moveAsBlockEntity(Level level, BlockPos pos, BlockPos newPos) {
        BlockState bs = level.getBlockState(pos);
        // destroy some leaves
        if (BlockTags.LEAVES.contains(bs.getBlock()) && level.getRandom().nextBoolean()) {
            Block.dropResources(bs, level, newPos, null);
        } else {
            CustomFallingBlockEntity e = new CustomFallingBlockEntity(level, newPos, bs);
            level.addFreshEntity(e);
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    class CustomFallingBlockEntity extends FallingBlockEntity {
        public CustomFallingBlockEntity(Level level, BlockPos pos, BlockState s) {
            // Adjust by 0.5 to snap to block axis
            super(level, pos.getX() + (pos.getX() < 0 ? 0.5 : -0.5), pos.getY(),
                    pos.getZ() + (pos.getZ() < 0 ? 0.5 : -0.5), s);
            this.setBoundingBox(new AABB(pos.offset(0, 0, 0), pos.offset(1, 1, 1)));
            this.time = 1;
        }

        @Nullable
        @Override
        public ItemEntity spawnAtLocation(ItemStack stack, float offsetY) {
            BlockState state = this.getBlockState();

            if (state != null && state.getBlock() instanceof LeavesBlock) {
                Block.dropResources(state, this.level, this.getOnPos(), null);
                return null;
            }

            return super.spawnAtLocation(stack, offsetY);
        }
    }
}
