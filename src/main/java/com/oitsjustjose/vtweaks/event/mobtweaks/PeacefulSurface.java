package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PeacefulSurface
{
    @SubscribeEvent
    public void registerTweak(LivingSpawnEvent event)
    {
        if (!ModConfig.mobTweaks.peacefulSurface.enabled)
        {
            return;
        }
        if (event.getEntity() == null || !(event.getEntity() instanceof EntityMob))
        {
            return;
        }
        for (int i : ModConfig.mobTweaks.peacefulSurface.blacklist)
        {
            if (i == event.getWorld().provider.getDimension())
            {
                return;
            }
        }
        if (event.getWorld().provider.getMoonPhase(event.getWorld().getWorldTime()) != 4
                && event.getEntity().getPosition().getY() >= ModConfig.mobTweaks.peacefulSurface.minY)
        {
            event.setResult(Event.Result.DENY);
        }
    }
}
