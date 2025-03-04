package com.oitsjustjose.vtweaks.common.tweaks.world;


import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@Tweak(category = "world")
public class StormDisableTweak extends VTweak {
    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.enabled = builder.comment("Disables thunder storms, converting storms to normal rain").define("enableStormTweak", true);
    }

    @SubscribeEvent
    public void process(LevelTickEvent.Pre evt) {
        if (!this.enabled.get()) return;
        if (!evt.getLevel().getLevelData().isThundering()) return;
        if (!(evt.getLevel().getLevelData() instanceof ServerLevelData levelData)) return;
        levelData.setThundering(false);
    }
}
