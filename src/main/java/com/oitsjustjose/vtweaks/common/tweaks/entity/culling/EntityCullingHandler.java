package com.oitsjustjose.vtweaks.common.tweaks.entity.culling;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

@Tweak(category = "entity")
public class EntityCullingHandler extends VTweak {
    public static final ArrayList<EntityCullingRule> AllRules = new ArrayList<>();

    @SubscribeEvent
    public void process(MobSpawnEvent.FinalizeSpawn evt) {
        if (evt.getLevel().isClientSide()) return;
        if (!(evt.getLevel() instanceof ServerLevel)) return;
        if (AllRules.stream().anyMatch(x -> x.apply(evt))) {
            evt.setResult(Event.Result.DENY);
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }
}
