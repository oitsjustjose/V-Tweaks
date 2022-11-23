package com.oitsjustjose.vtweaks.common.tweaks.world;


import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import com.oitsjustjose.vtweaks.common.tweaks.core.VTweak;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = TickEvent.LevelTickEvent.class, category = "world")
public class StormDisableTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enable;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enable = builder.comment("Disables thunder storms, fixing glitched lighting from thunder and other side-effects").define("enableStormTweak", true);
    }

    @Override
    public void process(Event event) {
        if (!this.enable.get()) return;
        var evt = (TickEvent.LevelTickEvent) event;
        if (evt.level == null) return;
        if (!evt.level.getLevelData().isThundering()) return;
        if (!(evt.level.getLevelData() instanceof ServerLevelData levelData)) return;
        levelData.setThundering(false);
    }
}
