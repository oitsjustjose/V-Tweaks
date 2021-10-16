package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class LowHealthSound {
    private static final String SOUND_KEY = "vtweaks:lowHealthSound";

    @SubscribeEvent
    public void registerEvent(PlayerEvent evt) {
        if (!ClientConfig.ENABLE_LOW_HEALTH_SOUND.get()) {
            return;
        }

        if (evt.getPlayer() == null || !evt.getPlayer().isAlive()) {
            return;
        }

        float vol = ClientConfig.LOW_HEALTH_VOLUME.get().floatValue();
        long now = System.currentTimeMillis();

        if (evt.getPlayer().getHealth() / evt.getPlayer().getMaxHealth() < ClientConfig.LOW_HEALTH_THRESHOLD.get()) {
            if (now >= getNextPlayTime(evt.getPlayer())) {
                BEATS type = getNextPlayType(evt.getPlayer());
                float pitch = type == BEATS.first ? 1F : .5F;
                long nextDelay = type == BEATS.first ? 200L : ClientConfig.LOW_HEALTH_FREQ.get();


                evt.getPlayer().playSound(SoundEvents.NOTE_BLOCK_BASEDRUM, vol, pitch);
                setNext(evt.getPlayer(), type == BEATS.first ? BEATS.second : BEATS.first, now + nextDelay);
            }
        }
    }

    private long getNextPlayTime(Player ent) {
        CompoundTag comp = ent.getPersistentData();
        if (comp.contains(SOUND_KEY)) {
            CompoundTag obj = comp.getCompound(SOUND_KEY);
            return obj.getLong("time");
        }
        return 0L;
    }

    private BEATS getNextPlayType(Player ent) {
        CompoundTag comp = ent.getPersistentData();
        if (comp.contains(SOUND_KEY)) {
            CompoundTag obj = comp.getCompound(SOUND_KEY);
            return BEATS.valueOf(obj.getString("type"));
        }
        return BEATS.first;
    }

    private void setNext(Player ent, BEATS b, long nextTime) {
        CompoundTag comp = ent.getPersistentData();
        CompoundTag obj = new CompoundTag();

        obj.putString("type", b.name());
        obj.putLong("time", nextTime);
        comp.put(SOUND_KEY, obj);
    }

    enum BEATS {
        first, second
    }
}
