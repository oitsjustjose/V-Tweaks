package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;

import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StormTweak {
    @SubscribeEvent
    public void registerTweak(WorldEvent event) {
        // Check if feature is enabled
        if (!CommonConfig.DISABLE_THUNDER_STORMS.get() || event.getWorld() == null) {
            return;
        }
        // Converts storms to regular rain
        if (event.getWorld() != null) {
            if (event.getWorld().getWorldInfo().isRaining()) {
                if (event.getWorld().getWorldInfo().isThundering()) {
                    if (event.getWorld().getWorldInfo() instanceof IServerWorldInfo) {
                        ((IServerWorldInfo) event.getWorld().getWorldInfo()).setThundering(false);
                    }
                }
            }
        }
    }
}
