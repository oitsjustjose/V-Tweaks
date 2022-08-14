package com.oitsjustjose.vtweaks.common.event;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DeathPoint {
    @SubscribeEvent
    public void registerTweak(LivingDeathEvent event) {
        if (!CommonConfig.ENABLE_DEATH_MESSAGE.get()) return;
        if (!(event.getEntity() instanceof Player player)) return;
        player.sendSystemMessage(getCoordMessage(player));
    }

    // Coincidentally compatible with Journeymap :D
    private MutableComponent getCoordMessage(Player player) {
        BlockPos pos = player.getOnPos();
        try {
            TranslatableContents contents = new TranslatableContents("vtweaks.death.message", pos.getX(), pos.getY(), pos.getZ());
            return contents.resolve(null, null, 0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return Component.empty().append(e.getMessage());
        }
    }
}