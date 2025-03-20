/**
 * @NOTE This is not a normal tweak!! It is registered via PowderedSnowMixin#PowderSnowBlock
 *
 * Also ensures that this is treated as a singleton -- if multiple (modded) PowderSnowBlock instances are created, we
 * only want to register this event once.
 *
 * This event is not registered if the mixin config is not enabled
 */

package com.oitsjustjose.vtweaks.common.tweaks.block;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class PowderSnowParticleHandler {
    private static boolean hasRegistered = false;

    public PowderSnowParticleHandler() {
        hasRegistered = true;
    }

    public static boolean hasRegistered() {
        return hasRegistered;
    }

    @SubscribeEvent
    public void registerEvent(PlayerTickEvent.Post evt) {
        var player = evt.getEntity();
        var level = player.level();

        var onState = level.getBlockState(player.getBlockPosBelowThatAffectsMyMovement());
        if (!(onState.getBlock() instanceof PowderSnowBlock)) return;

        // Code yoinked from PowderSnowBlock#entityInside:
        var random = player.level().getRandom();
        var hasMoved = player.xOld != player.getX() || player.zOld != player.getZ();

        if (!(hasMoved && random.nextBoolean())) return;
        player.level().addParticle(
                ParticleTypes.SNOWFLAKE,
                player.getX(),
                player.getY(),
                player.getZ(),
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083F,
                0.05D,
                Mth.randomBetween(random, -1.0F, 1.0F) * 0.083F
        );
    }
}
