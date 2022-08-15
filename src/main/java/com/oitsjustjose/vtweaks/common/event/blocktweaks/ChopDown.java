/**
 * Ported from https://github.com/ternsip/ChopDown/
 * <p>
 * Lots of code here is from there but in my own interpreted way
 */

package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class ChopDown {
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (!BlockTweakConfig.ENABLE_TREE_CHOP_DOWN.get()) return;

        int lumberLvl = evt.getPlayer().getMainHandItem().getEnchantmentLevel(VTweaks.Enchantments.lumbering);

        if (evt.getPlayer().isCrouching() && lumberLvl > 0) return;
        if (!(evt.getLevel() instanceof Level level)) return;

        BlockPos initialPos = evt.getPos();
        Player player = evt.getPlayer();

        for (int dy = 0; dy < BlockTweakConfig.TREE_CHOP_DOWN_LOG_COUNT.get(); dy++) {
            if (!level.getBlockState(initialPos.offset(0, dy, 0)).is(BlockTags.LOGS)) return;
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
                        boolean isLog = bs.is(BlockTags.LOGS);
                        boolean isLeaf = bs.is(BlockTags.LEAVES);

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
        return switch (facing) {
            case SOUTH -> pos.offset(1, 0, 1 + transformedY);
            case NORTH -> pos.offset(1, 0, -transformedY + 1);
            case EAST -> pos.offset(1 + transformedY, 0, 1);
            case WEST -> pos.offset(-transformedY + 1, 0, 1);
            default -> pos; // UP and DOWN not needed;
        };
    }

    private boolean canBeMoved(Level level, BlockPos pos) {
        BlockState bs = level.getBlockState(pos);
        return !bs.hasBlockEntity() && (bs.is(BlockTags.LOGS) || bs.is(BlockTags.LEAVES)
                || bs.is(BlockTags.BEEHIVES) || bs.isAir());
    }

    private void moveAsBlockEntity(Level level, BlockPos pos, BlockPos newPos) {
        BlockState bs = level.getBlockState(pos);
        // destroy some leaves
        if (bs.is(BlockTags.LEAVES) && level.getRandom().nextBoolean()) {
            Block.dropResources(bs, level, newPos, null);
        } else {
            FallingBlockEntity e = FallingBlockEntity.fall(level, newPos, bs);
            level.addFreshEntity(e);
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }
}
