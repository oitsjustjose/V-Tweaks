package com.oitsjustjose.vtweaks.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.oitsjustjose.vtweaks.common.config.ClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LowHealthSound {
    Minecraft mc = Minecraft.getInstance();
    Executor ex = Executors.newSingleThreadExecutor();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(PlayerEvent evt) {
        if (!ClientConfig.ENABLE_LOW_HEALTH_SOUND.get()) {
            return;
        }

        if (evt.getPlayer() == null || mc.player == null || !evt.getPlayer().isAlive()) {
            return;
        }

        ex.execute(() -> {
            try {
                if (!mc.isGamePaused()) {
                    float vol = ClientConfig.LOW_HEALTH_VOLUME.get().floatValue();
                    if (evt.getPlayer().getHealth() / evt.getPlayer().getMaxHealth() < ClientConfig.LOW_HEALTH_THRESHOLD
                            .get()) {
                        mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, vol, .5F);
                        Thread.sleep(200);

                        mc.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM, vol, 1F);
                        Thread.sleep(ClientConfig.LOW_HEALTH_FREQ.get());
                    }
                }
            } catch (InterruptedException ex) {
            }
        });
    }
}
