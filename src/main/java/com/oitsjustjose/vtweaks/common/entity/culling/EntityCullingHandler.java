package com.oitsjustjose.vtweaks.common.entity.culling;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class EntityCullingHandler {
    public static ArrayList<EntityCullingRule> rules = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerEvent(LivingSpawnEvent evt) {
        if (evt.getWorld().isClientSide() || !(evt.getWorld() instanceof ServerLevel)) {
            return;
        }
        rules.forEach(x -> x.apply(evt));
    }
}
