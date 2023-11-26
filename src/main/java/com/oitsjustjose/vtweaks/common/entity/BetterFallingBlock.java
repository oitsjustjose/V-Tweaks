package com.oitsjustjose.vtweaks.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BetterFallingBlock extends FallingBlockEntity {
    private BlockState heldState;

    public BetterFallingBlock(Level level, @Nonnull BlockState heldState) {
        super(EntityType.FALLING_BLOCK, level);
        super.setUUID(UUID.randomUUID());
        this.heldState = heldState;
    }

    public BetterFallingBlock(Level level, double x, double y, double z, @Nonnull BlockState heldState) {
        this(level, heldState);
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static BetterFallingBlock fall(Level level, BlockPos pos, BlockState heldState) {
        var ent = new BetterFallingBlock(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, heldState.hasProperty(BlockStateProperties.WATERLOGGED) ? heldState.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE) : heldState);
        level.setBlock(pos, heldState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(ent);
        return ent;
    }

    @Override
    public @NotNull BlockState getBlockState() {
        return this.heldState;
    }

    @Override
    public void tick() {
        if (this.heldState.isAir()) this.discard();

        var block = this.heldState.getBlock();
        ++this.time;

        if (!this.isNoGravity()) this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!this.getLevel().isClientSide) {
            BlockPos blockpos = this.blockPosition();

            if (!this.isOnGround()) {
                if (!this.getLevel().isClientSide && (this.time > 100 && (blockpos.getY() <= this.getLevel().getMinBuildHeight() || blockpos.getY() > this.getLevel().getMaxBuildHeight()) || this.time > 600)) {
                    if (this.dropItem && this.getLevel().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        if (this.getLevel() instanceof ServerLevel serverLevel) {
                            Block.getDrops(this.heldState, serverLevel, blockpos, null).forEach(this::spawnAtLocation);
                        }
                    }

                    this.discard();
                }
            } else {
                var state = this.getLevel().getBlockState(blockpos);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                boolean canReplace = state.canBeReplaced(new DirectionalPlaceContext(this.getLevel(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                boolean canBePlacedHere = this.heldState.canSurvive(this.getLevel(), blockpos) && !FallingBlock.isFree(this.getLevel().getBlockState(blockpos.below()));
                if (canReplace && canBePlacedHere) {
                    /* Waterlog if it fell into water and is waterloggable */
                    if (this.heldState.hasProperty(BlockStateProperties.WATERLOGGED) && this.getLevel().getFluidState(blockpos).getType() == Fluids.WATER) {
                        this.heldState = this.heldState.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
                    }
                    /* Set block and notify */
                    if (this.getLevel().setBlock(blockpos, this.heldState, 3)) {
                        ((ServerLevel) this.getLevel()).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(blockpos, this.getLevel().getBlockState(blockpos)));
                        this.discard();
                        if (block instanceof Fallable) {
                            ((Fallable) block).onLand(this.getLevel(), blockpos, this.heldState, state, this);
                        }
                    } else if (this.dropItem && this.getLevel().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        this.discard();
                        this.callOnBrokenAfterFall(block, blockpos);
                        if (this.getLevel() instanceof ServerLevel serverLevel) {
                            Block.getDrops(this.heldState, serverLevel, blockpos, null).forEach(this::spawnAtLocation);
                        }
                    }
                } else {
                    this.discard();
                    if (this.dropItem && this.getLevel().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        this.callOnBrokenAfterFall(block, blockpos);
                        if (this.getLevel() instanceof ServerLevel serverLevel) {
                            Block.getDrops(this.heldState, serverLevel, blockpos, null).forEach(this::spawnAtLocation);
                        }
                    }
                }
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
    }
}
