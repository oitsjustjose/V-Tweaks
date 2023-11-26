package com.oitsjustjose.vtweaks.common.tweaks.block;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.entity.BetterFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Tweak(category = "block.chopdown")
public class ChopDownTweak extends VTweak {

    public ForgeConfigSpec.BooleanValue enabled;
    public ForgeConfigSpec.IntValue logCount;
    public ForgeConfigSpec.IntValue leafSearchRad;
    public ForgeConfigSpec.BooleanValue requireTool;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Trees fall down (like, actually not just like lumbering). Credit to Ternsip's impl (https://www.curseforge.com/minecraft/mc-mods/chopdown)").define("enableTreeChopDown", true);
        this.logCount = builder.comment("The number of logs above the one broken to trigger the chopdown effect").defineInRange("chopDownLogRequirement", 3, 1, Integer.MAX_VALUE);
        this.leafSearchRad = builder.comment("The radius that this tweak will use to attempt to find leaves. Set this to a large number to detect larger trees (may cause lag)").defineInRange("chopdownSearchRadius", 64, 1, Integer.MAX_VALUE);
        this.requireTool = builder.comment("If set to true, ChopDown will only work when the player is using the right tool for the log").define("requiresRightTool", false);
        builder.pop();
    }

    @SubscribeEvent
    public void process(BlockEvent.BreakEvent evt) {
        if (!this.enabled.get()) return;

        var level = evt.getLevel();
        var initPos = evt.getPos();
        var player = evt.getPlayer();

        // Check to see if the player is using the right tool to yield drops. Ignore if they aren't.
        if (this.requireTool.get() && !isBestTool(evt.getState(), player.getMainHandItem())) return;

        /* Verify that there are enough logs to consider this a tree */
        for (int dy = 0; dy < this.logCount.get(); dy++) {
            if (!level.getBlockState(initPos.offset(0, dy, 0)).is(BlockTags.LOGS)) return;
        }

        var leaves = 5;
        var rad = this.leafSearchRad.get();
        var foundLeaves = false;
        var queue = new LinkedList<BlockPos>();
        var used = new HashMap<BlockPos, Integer>();

        queue.add(initPos);
        used.put(initPos, leaves);

        while (!queue.isEmpty()) {
            var top = queue.pollFirst();
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        var tmp = top.offset(dx, dy, dz);
                        var step = used.get(top);
                        if (step <= 0 || tmp.distSqr(initPos) > (rad * rad)) {
                            continue;
                        }

                        var state = level.getBlockState(tmp);
                        var isLog = state.is(BlockTags.LOGS);
                        var isLeaf = state.is(BlockTags.LEAVES) && (!state.hasProperty(LeavesBlock.PERSISTENT) || !state.getValue(LeavesBlock.PERSISTENT));
                        if (isLeaf && !foundLeaves) foundLeaves = true;

                        if ((dy >= 0 && step == leaves && isLog) || isLeaf) {
                            step -= (isLeaf ? 1 : 0);
                            if (!used.containsKey(tmp) || used.get(tmp) < step) {
                                used.put(tmp, step);
                                queue.push(tmp);
                            }
                        }
                    }
                }
            }
        }

        if (!foundLeaves) return;

        var dir = directionFromPosDiff(player.blockPosition(), initPos);
        used.forEach((pos, cnt) -> {
            if (!initPos.equals(pos) && canBeMoved(level, pos.offset(0, -1, 0))) {
                var transformed = getTransform(initPos, pos, dir);
                moveAsBlockEntity(level, pos, transformed, dir);
            }
        });
    }

    /**
     * Moves a block as an Entity for effect
     *
     * @param level     The level in which the event occurred
     * @param pos       The position where the block started
     * @param newPos    The new position where the block will be
     * @param direction The direction in which the event occurred
     */
    private void moveAsBlockEntity(LevelAccessor level, BlockPos pos, BlockPos newPos, Direction direction) {
        var bs = rotate(level, pos, level.getBlockState(pos), direction);
        // destroy some leaves
        if (bs.is(BlockTags.LEAVES) && level.getRandom().nextBoolean()) {
            Block.dropResources(bs, level, newPos, null);
        } else {
            var falling = BetterFallingBlock.fall((Level) level, newPos, bs);
            level.addFreshEntity(falling);
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    /**
     * Gets the position of a block after its corresponding tree is transposed
     *
     * @param initialBreakPos The pos of the initial log breakage (bottom of "tree")
     * @param pos             The position of the current block in need of transform
     * @param direction       The direction of the event
     * @return The input Pos's new BlockPos for the now fallen tree
     */
    private BlockPos getTransform(BlockPos initialBreakPos, BlockPos pos, Direction direction) {
        // Transforms height of tree into length of throw
        var transformedY = pos.getY() - initialBreakPos.getY();
        return switch (direction) {
            case SOUTH -> pos.offset(0, 0, 1 + transformedY);
            case NORTH -> pos.offset(0, 0, -transformedY - 1);
            case EAST -> pos.offset(1 + transformedY, 0, 0);
            case WEST -> pos.offset(-transformedY - 1, 0, 0);
            default -> pos; // UP and DOWN not needed;
        };
    }

    /**
     * Performs a check to see if a block is valid for movement as an entity
     *
     * @param level The level in which the event occurred
     * @param pos   The position of the block in question
     * @return True if the block can be moved, false otherwise
     */
    private boolean canBeMoved(LevelAccessor level, BlockPos pos) {
        var bs = level.getBlockState(pos);
        return !bs.hasBlockEntity() && (bs.is(BlockTags.LOGS) || (bs.is(BlockTags.LEAVES) && bs.hasProperty(LeavesBlock.PERSISTENT) && !bs.getValue(LeavesBlock.PERSISTENT)) || bs.is(BlockTags.BEEHIVES) || bs.isAir());
    }

    /**
     * Rotates a BlockState if possible
     *
     * @param level     The event level
     * @param pos       The location of the event (used only by BlockState#rotate)
     * @param state     The current state being checked for rotation
     * @param direction The direction of the event
     * @return The passed in BlockState, now rotated if possible
     */
    private BlockState rotate(LevelAccessor level, BlockPos pos, BlockState state, Direction direction) {
        if (state.hasProperty(RotatedPillarBlock.AXIS)) {
            var axis = state.getValue(RotatedPillarBlock.AXIS);
            var ret = state.setValue(RotatedPillarBlock.AXIS, axis == Direction.Axis.X ? Direction.Axis.Y : Direction.Axis.X);
            return switch (direction) {
                case NORTH, SOUTH -> ret.rotate(level, pos, Rotation.CLOCKWISE_90);
                case EAST, WEST -> ret;
                default -> state;
            };
        }
        return state;
    }

    /**
     * Guesstimates the Direction of the Break event
     *
     * @param playerPos The player position
     * @param brokenPos The position of the block broken
     * @return The direction the breakage likely occurred
     */
    private Direction directionFromPosDiff(BlockPos playerPos, BlockPos brokenPos) {
        var x = (brokenPos.getX() + 0.5) - playerPos.getX();
        var z = (brokenPos.getZ() + 0.5) - playerPos.getZ();
        var abX = Math.abs(x);
        var abZ = Math.abs(z);
        var dirX = abX > abZ ? (int) Math.floor(abX / x) : 0;
        var dirZ = abX > abZ ? 0 : (int) Math.floor(abZ / z);
        if (dirX != 0) {
            return dirX == 1 ? Direction.EAST : Direction.WEST;
        }
        return dirZ == 1 ? Direction.SOUTH : Direction.NORTH;
    }


    private static boolean isBestTool(BlockState stateIn, ItemStack toolIn) {
        if (toolIn.isEmpty()) return false;

        var searchKey = ":mineable/";
        var mineableKey = stateIn.getTags().filter(x -> x.location().toString().contains(searchKey)).findFirst();
        if (mineableKey.isEmpty()) {
            VTweaks.getInstance().LOGGER.warn(
                    "Failed to find mineable tag on block {} with tags {}",
                    stateIn.getBlock().getDescriptionId(),
                    stateIn.getTags().map(x -> x.location().toString()).collect(Collectors.joining(", "))
            );
            return false;
        }

        var toolType = mineableKey.get().location().toString();
        toolType = toolType.substring(toolType.indexOf(searchKey) + searchKey.length());

        var variants = Arrays.asList(
                ItemTags.create(new ResourceLocation("minecraft", toolType + "s")),
                ItemTags.create(new ResourceLocation("forge", "tools/" + toolType + "s"))
        );

        for (var variant : variants) {
            if (toolIn.is(variant)) return true;
        }

        return false;
    }
}
