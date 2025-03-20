package com.oitsjustjose.vtweaks.common.tweaks.block;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Tweak(category = "" /* No config */)
public class PowderSnowParticleHandler extends VTweak {
    @SubscribeEvent
    public void registerEvent(PlayerTickEvent.Post evt) {
        if (!CommonConfig.EnablePowderedSnowMixin.get()) return;

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
