package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PeacefulSurface {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void registerTweak(LivingSpawnEvent event) {
        if (!MobTweakConfig.ENABLE_PEACEFUL_SURFACE.get()) {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity() instanceof MonsterEntity)) {
            return;
        }
        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get()) {
            if (event.getWorld().getDimension().getType().getRegistryName().toString().equalsIgnoreCase(dimType)) {
                return;
            }
        }

        MonsterEntity monster = (MonsterEntity) event.getEntity();
        // Check for midnight
        if (event.getWorld().getWorld().getDimension().getMoonPhase(event.getWorld().getWorld().getDayTime()) != 4) {
            // Check if position is high enough
            if (event.getEntity().getPosition().getY() >= MobTweakConfig.PEACEFUL_SURFACE_MIN_Y.get()) {
                // Check if it's a natural spawn
                CompoundNBT comp = monster.getPersistentData();

                if (comp.contains("peacefulSurfaceRemove") && comp.getBoolean("peacefulSurfaceRemove")) {
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerTweak(LivingSpawnEvent.CheckSpawn event) {
        if (!MobTweakConfig.ENABLE_PEACEFUL_SURFACE.get()) {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity() instanceof MonsterEntity)) {
            return;
        }

        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get()) {
            if (event.getWorld().getDimension().getType().getRegistryName().toString().equalsIgnoreCase(dimType)) {
                return;
            }
        }

        MonsterEntity monster = (MonsterEntity) event.getEntity();
        // Flag any natural mob spawns
        if (event.getSpawnReason() == SpawnReason.NATURAL) {
            CompoundNBT comp = monster.getPersistentData();

            if (!comp.contains("peacefulSurfaceRemove")) {
                comp.putBoolean("peacefulSurfaceRemove", true);
            }
        }
    }
}
