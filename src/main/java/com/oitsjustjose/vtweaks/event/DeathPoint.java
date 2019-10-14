package com.oitsjustjose.vtweaks.event;

import com.oitsjustjose.vtweaks.config.CommonConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathPoint
{
    @SubscribeEvent
    public void registerTweak(LivingDeathEvent event)
    {
        // Check if feature is enabled
        if (!CommonConfig.ENABLE_DEATH_MESSAGE.get())
        {
            return;
        }
        // Are you human?
        if (!(event.getEntity() instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntity();
        player.sendMessage(getCoordMessage(event.getEntity().getPosition()));
    }

    // Coincidentally compatible with Journeymap :D
    private TranslationTextComponent getCoordMessage(BlockPos pos)
    {
        return new TranslationTextComponent("vtweaks.death.message", pos.getX(), pos.getY(), pos.getZ());
    }
}