package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StormTweak {
    @SubscribeEvent
    public void registerTweak(LevelEvent event) {
        // Check if feature is enabled
        if (!CommonConfig.DISABLE_THUNDER_STORMS.get() || event.getLevel() == null) {
            return;
        }
        // Converts storms to regular rain
        if (event.getLevel() != null) {
            if (event.getLevel().getLevelData().isRaining()) {
                if (event.getLevel().getLevelData().isThundering()) {
                    if (event.getLevel().getLevelData() instanceof ServerLevelData) {
                        ((ServerLevelData) event.getLevel().getLevelData()).setThundering(false);
                    }
                }
            }
        }
    }
}
