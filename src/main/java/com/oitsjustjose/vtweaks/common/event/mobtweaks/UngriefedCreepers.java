package com.oitsjustjose.vtweaks.common.event.mobtweaks;


import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class UngriefedCreepers {
    private final ConcurrentLinkedQueue<ExplodedBlock> pending;

    public UngriefedCreepers() {
        this.pending = new ConcurrentLinkedQueue<>();
    }

    @SubscribeEvent
    public void registerEvent(ExplosionEvent evt) {
        if (evt.getExplosion() == null || evt.getExplosion().getSourceMob() == null) {
            return;
        }

        if (!MobTweakConfig.UNGRIEFED_CREEPERS.get()) {
            return;
        }

        LivingEntity exploder = evt.getExplosion().getSourceMob();
        Level lvl = exploder.getLevel();

        if (!exploder.getType().is(Constants.CREEPERS)) {
            return;
        }

        AtomicInteger idx = new AtomicInteger();
        // Enqueue future block placements from Lowest Y, Increment
        evt.getExplosion().getToBlow().sort(Comparator.comparingInt(Vec3i::getY));
        evt.getExplosion().getToBlow().forEach(pos -> {
            BlockState state = lvl.getBlockState(pos);
            BlockEntity ent = lvl.getBlockEntity(pos);
            if (!state.isAir()) {
                this.pending.add(new ExplodedBlock(state, pos, lvl, idx.get(), ent));
                idx.getAndIncrement();
            }
        });

        // Destroy current blocks from Highest Y, Decrement
        Collections.sort(evt.getExplosion().getToBlow(), (a, b) -> Integer.compare(b.getY(), a.getY()));
        evt.getExplosion().getToBlow().forEach(pos -> {
            lvl.removeBlockEntity(pos);
            lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            lvl.setBlocksDirty(pos, lvl.getBlockState(pos), Blocks.AIR.defaultBlockState());
        });

        evt.getExplosion().clearToBlow();
    }


    @SubscribeEvent
    public void registerEvent(TickEvent evt) {
        List<ExplodedBlock> toRemove = new ArrayList<>();

        for (ExplodedBlock b : this.pending) {
            if (b.tick()) {
                toRemove.add(b);
            }
        }

        this.pending.removeAll(toRemove);
    }

    public static class ExplodedBlock {
        private final BlockState state;
        private final BlockPos pos;
        private final BlockEntity ent;
        private final Level lvl;
        private int ticks;

        public ExplodedBlock(BlockState s, BlockPos p, Level l, int idx, @Nullable BlockEntity e) {
            this.state = s;
            this.pos = p;
            this.lvl = l;
            this.ent = e;
            this.ticks = -idx * 10;
            assert this.ent == null || this.ent.getBlockPos() == this.pos;
        }

        public boolean tick() {
            final int delay = 500;
            int numTicksRequired = (int) (Objects.requireNonNull(lvl.getServer()).getAverageTickTime() * delay);
            if (this.ticks >= numTicksRequired) {
                this.restore();
                return true;
            } else {
                this.ticks++;
                return false;
            }
        }

        public BlockPos getPos() {
            return this.pos;
        }

        private void restore() {
            this.lvl.setBlock(this.pos, this.state, Block.UPDATE_CLIENTS | 1024);
            this.lvl.setBlocksDirty(this.pos, lvl.getBlockState(this.pos), this.state);
            this.lvl.playSound(null, this.pos, this.state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.15F, 1.0F);
            if (this.ent != null) {
                this.lvl.setBlockEntity(this.ent);
            }
        }
    }
}