package com.oitsjustjose.vtweaks.common.tweaks.world;


import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = TickEvent.LevelTickEvent.class, category = "world")
public class StormDisableTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enable;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enable = builder.comment("Disables thunder storms, fixing glitched lighting from thunder and other side-effects").define("enableStormTweak", true);
    }

    @SubscribeEvent
    public void process(TickEvent.LevelTickEvent evt) {
        if (!this.enable.get()) return;
        if (evt.level == null) return;
        if (!evt.level.getLevelData().isThundering()) return;
        if (!(evt.level.getLevelData() instanceof ServerLevelData levelData)) return;
        levelData.setThundering(false);
    }
}
