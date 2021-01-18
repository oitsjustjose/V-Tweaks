package com.oitsjustjose.vtweaks.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LowHealthSound {
    private long lastSound = 0L;
    Minecraft mc = Minecraft.getInstance();
    Executor ex = Executors.newSingleThreadExecutor();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(PlayerEvent evt) {
        // TODO: Early exit for config option
        // TODO: Use config option for threshold
        // TODO: Use config option for frequency of sound
        if (evt.getPlayer() == null || mc.player == null || !evt.getPlayer().isAlive()) {
            return;
        }

        long time = System.currentTimeMillis();
        if (time - lastSound < 2000L) {
            return;
        }

        // Play a sound if
        if (evt.getPlayer().getHealth() / evt.getPlayer().getMaxHealth() < .20F) {
            ex.execute(() -> {
                // mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT,
                // mc.gameSettings.getSoundLevel(SoundCategory.VOICE), .75F);
                // mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING,
                // mc.gameSettings.getSoundLevel(SoundCategory.VOICE), .5F);
                mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, .5F, 2F);
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 200L) {
                    ;
                }
                mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, 1F, 2F);
                lastSound = time;
                return;
            });
        }

    }
}
