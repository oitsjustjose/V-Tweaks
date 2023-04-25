package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.TickScheduler;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

@Tweak(eventClass = ExplosionEvent.Detonate.class, category = "entity")
public class GrieflessCreeperTweak extends VTweak {
    public static final TagKey<EntityType<?>> CREEPERS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", "creepers"));
    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("When any Creeper (or entity with EntityType tag #forge:creepers) explodes, all blocks destroyed will plop back into place after a few seconds!").define("ungriefCreepers", true);
    }

    @SubscribeEvent
    public void process(ExplosionEvent evt) {
        if (!this.enabled.get()) return;

        if (evt.getExplosion() == null) return;
        if (evt.getExplosion().getDamageSource().getEntity() == null) return;
        if (!evt.getExplosion().getDamageSource().getEntity().getType().is(CREEPERS)) return;

        var exploder = evt.getExplosion().getDamageSource().getEntity();
        var lvl = exploder.getLevel();
        var idx = new AtomicInteger();

        // Enqueue future block placements from Lowest Y, Increment
        evt.getExplosion().getToBlow().sort(Comparator.comparingInt(Vec3i::getY));
        evt.getExplosion().getToBlow().forEach(pos -> {
            var state = lvl.getBlockState(pos);
            var ent = lvl.getBlockEntity(pos);
            if (!state.isAir()) {
                var restoreTask = new TickScheduler.ScheduledTask(() -> {
                    lvl.setBlock(pos, state, Block.UPDATE_CLIENTS | 1024);
                    lvl.setBlocksDirty(pos, lvl.getBlockState(pos), state);
                    lvl.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.15F, 1.0F);
                    if (ent != null) lvl.setBlockEntity(ent);
                }, 5 + (idx.get() / 25F)); /* 25F => 25 blocks restored per second */
                VTweaks.getInstance().Scheduler.addTask(restoreTask);
                idx.getAndIncrement();
            }
        });

        // Destroy current blocks from Highest Y, Decrement
        evt.getExplosion().getToBlow().sort((a, b) -> Integer.compare(b.getY(), a.getY()));
        evt.getExplosion().getToBlow().forEach(pos -> {
            lvl.removeBlockEntity(pos);
            lvl.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            lvl.setBlocksDirty(pos, lvl.getBlockState(pos), Blocks.AIR.defaultBlockState());
        });

        evt.getExplosion().clearToBlow();
    }
}
