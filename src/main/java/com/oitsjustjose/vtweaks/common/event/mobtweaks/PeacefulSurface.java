package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
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
            if (event.getWorld().func_230315_m_().func_242725_p().toString().equalsIgnoreCase(dimType)) {
                return;
            }
        }

        MonsterEntity monster = (MonsterEntity) event.getEntity();

        if (event.getWorld() == null || ((World) event.getWorld()).getServer() == null) {
            return;
        }

        int day = (int) (event.getWorld().getWorldInfo().getDayTime() / 24000L % 2147483647L);

        // Check for midnight
        if (!(day % 4 == 0 && day % 8 != 0)) {
            // Check if position is high enough
            if (event.getEntity().func_233580_cy_().getY() >= MobTweakConfig.PEACEFUL_SURFACE_MIN_Y.get()) {
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
            if (event.getWorld().func_230315_m_().func_242725_p().toString().equalsIgnoreCase(dimType)) {
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
