package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StormTweak {
    @SubscribeEvent
    public void registerTweak(LevelEvent event) {
        if (!CommonConfig.DISABLE_THUNDER_STORMS.get()) return;
        if (event.getLevel() == null) return;
        // Converts storms to regular rain
        if (event.getLevel() != null) {
            if (event.getLevel().getLevelData().isRaining()) {
                if (event.getLevel().getLevelData().isThundering()) {
                    if (event.getLevel().getLevelData() instanceof ServerLevelData data) {
                        data.setThundering(false);
                    }
                }
            }
        }
    }
}
