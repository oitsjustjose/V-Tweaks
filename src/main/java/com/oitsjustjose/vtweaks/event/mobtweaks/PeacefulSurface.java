package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PeacefulSurface
{
    @SubscribeEvent
    public void registerTweak(LivingSpawnEvent event)
    {
        if (!ModConfig.mobTweaks.enablePeacefulSurface)
        {
            return;
        }
        
        Entity toKill = event.getEntity();
        if (toKill == null)
        {
            return;
        }
        if (event.getWorld().getMoonPhase() != 4 && toKill.getPosition().getY() >= event.getWorld().getTopSolidOrLiquidBlock(toKill.getPosition()).getY())
        {
            event.setResult(Event.Result.DENY);
        }
    }
}
