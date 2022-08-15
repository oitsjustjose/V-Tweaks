package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PeacefulSurface {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void registerTweak(LivingSpawnEvent.CheckSpawn event) {
        // Feature is enabled
        if (!MobTweakConfig.ENABLE_PEACEFUL_SURFACE.get()) return;
        // Entity exists
        if (event.getEntity() == null) return;
        // Spawn is a natural spawn
        if (event.getSpawnReason() != MobSpawnType.NATURAL) return;
        // Entity is a Monster
        if (!(event.getEntity() instanceof Monster)) return;
        // World is a server-world
        if (!(event.getLevel() instanceof ServerLevel lvl)) return;

        ResourceLocation dimName = lvl.dimension().location();
        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get()) {
            if (dimName.toString().equals(dimType)) return;
        }

        int day = (int) (lvl.getLevelData().getDayTime() / 24000L % 2147483647L);

        if (!(day % 4 == 0 && day % 8 != 0)) { // Check for midnight
            if (event.getEntity().getY() >= MobTweakConfig.PEACEFUL_SURFACE_MIN_Y.get()) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
