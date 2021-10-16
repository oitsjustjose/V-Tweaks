package com.oitsjustjose.vtweaks.common.event;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class DeathPoint {
    @SubscribeEvent
    public void registerTweak(LivingDeathEvent event) {
        // Check if feature is enabled
        if (!CommonConfig.ENABLE_DEATH_MESSAGE.get()) {
            return;
        }
        // Are you human?
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        player.sendMessage(getCoordMessage(player.getOnPos()), UUID.randomUUID());
    }

    // Coincidentally compatible with Journeymap :D
    private TranslatableComponent getCoordMessage(BlockPos pos) {
        return new TranslatableComponent("vtweaks.death.message", pos.getX(), pos.getY(), pos.getZ());
    }
}