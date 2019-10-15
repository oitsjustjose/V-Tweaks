package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PeacefulSurface
{
    @SubscribeEvent
    public void registerTweak(LivingSpawnEvent event)
    {
        if (!MobTweakConfig.ENABLE_PEACEFUL_SURFACE.get())
        {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity() instanceof MobEntity))
        {
            return;
        }
        for (String dimType : MobTweakConfig.PEACEFUL_SURFACE_BLACKLIST.get())
        {
            if (event.getWorld().getDimension().getType().getRegistryName().toString().equalsIgnoreCase(dimType))
            {
                return;
            }
        }
        if (event.getEntity().getEntityWorld().getCurrentMoonPhaseFactor() != 0.0
                && event.getEntity().getPosition().getY() >= MobTweakConfig.PEACEFUL_SURFACE_MIN_Y.get())
        {
            event.setResult(Event.Result.DENY);
        }
    }
}
