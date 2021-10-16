package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
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
        if (event.getEntity() == null || !(event.getEntity() instanceof Monster)) {
            return;
        }
        if (!(event.getWorld() instanceof ServerLevel)) {
            return;
        }
        ResourceLocation dimName = ((ServerLevel) event.getWorld()).dimension().location();
        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get()) {
            if (dimName.toString().equals(dimType)) {
                return;
            }
        }

        Monster monster = (Monster) event.getEntity();

        if (event.getWorld() == null || ((Level) event.getWorld()).getServer() == null) {
            return;
        }

        int day = (int) (event.getWorld().getLevelData().getDayTime() / 24000L % 2147483647L);

        // Check for midnight
        if (!(day % 4 == 0 && day % 8 != 0)) {
            // Check if position is high enough
            if (event.getEntity().getY() >= MobTweakConfig.PEACEFUL_SURFACE_MIN_Y.get()) {
                // Check if it's a natural spawn
                CompoundTag comp = monster.getPersistentData();

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
        if (event.getEntity() == null || !(event.getEntity() instanceof Monster)) {
            return;
        }
        if (!(event.getWorld() instanceof ServerLevel)) {
            return;
        }
        ResourceLocation dimName = ((ServerLevel) event.getWorld()).dimension().location();

        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get()) {
            if (dimName.toString().equals(dimType)) {
                return;
            }
        }

        Monster monster = (Monster) event.getEntity();
        // Flag any natural mob spawns
        if (event.getSpawnReason() == MobSpawnType.NATURAL) {
            CompoundTag comp = monster.getPersistentData();

            if (!comp.contains("peacefulSurfaceRemove")) {
                comp.putBoolean("peacefulSurfaceRemove", true);
            }
        }
    }
}
