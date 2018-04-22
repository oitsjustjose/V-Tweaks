package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StormTweak
{
    @SubscribeEvent
    public void registerTweak(WorldEvent event)
    {
        // Check if feature is enabled
        if (!ModConfig.misc.enableStormTweak)
        {
            return;
        }
        // Converts storms to regular rain
        if (event.getWorld().isRaining())
        {
            if (event.getWorld().getWorldInfo().isThundering())
            {
                event.getWorld().getWorldInfo().setThundering(false);
            }
        }
    }
}
