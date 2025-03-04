package com.oitsjustjose.vtweaks.common.tweaks.entity.culling;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

import java.util.ArrayList;

@Tweak(category = "entity")
public class EntityCullingHandler extends VTweak {
    public static final ArrayList<EntityCullingRule> AllRules = new ArrayList<>();

    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows use of datapacks to prevent a given types of entities from spawning in given biomes. The built-in datapack does not cull any entities.\nSee https://mods.oitsjustjose.com/V-Tweaks/#entity-culling for datapack documentation.").define("enableDataDrivenEntityCulling", true);
    }

    @SubscribeEvent
    public void process(FinalizeSpawnEvent evt) {
        if (!this.enabled.get()) return;
        if (evt.getLevel().isClientSide()) return;
        if (!(evt.getLevel() instanceof ServerLevel)) return;
        if (AllRules.stream().anyMatch(x -> x.apply(evt))) {
            evt.setSpawnCancelled(true);
            evt.setCanceled(true);
        }
    }
}
