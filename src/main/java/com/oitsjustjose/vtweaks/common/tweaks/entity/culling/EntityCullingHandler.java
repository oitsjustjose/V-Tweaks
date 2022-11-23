package com.oitsjustjose.vtweaks.common.tweaks.entity.culling;

import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import com.oitsjustjose.vtweaks.common.tweaks.core.VTweak;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;

@Tweak(eventClass = LivingSpawnEvent.CheckSpawn.class, category = "entity")
public class EntityCullingHandler extends VTweak {
    public static final ArrayList<EntityCullingRule> AllRules = new ArrayList<>();

    @Override
    public void process(Event event) {
        var evt = (LivingSpawnEvent.CheckSpawn) event;
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
