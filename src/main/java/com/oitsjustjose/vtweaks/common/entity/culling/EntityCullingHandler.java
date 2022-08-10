package com.oitsjustjose.vtweaks.common.entity.culling;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class EntityCullingHandler {
    public static ArrayList<EntityCullingRule> rules = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent.CheckSpawn evt) {
        if (evt.getWorld().isClientSide() || !(evt.getWorld() instanceof ServerLevel)) {
            return;
        }

        if (rules.stream().anyMatch(x -> x.apply(evt))) {
            evt.setResult(Event.Result.DENY);
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }
}
