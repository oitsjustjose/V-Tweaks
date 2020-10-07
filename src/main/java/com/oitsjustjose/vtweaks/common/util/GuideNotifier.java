package com.oitsjustjose.vtweaks.common.util;

import java.util.UUID;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.world.capability.IVTweaksCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuideNotifier {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!CommonConfig.ENABLE_WELCOME_MESSAGE.get()) {
            return;
        }

        IVTweaksCapability capability = event.getPlayer().getEntityWorld().getCapability(VTweaks.VTWEAKS_CAPABILITY)
                .orElse(null);

        if (capability == null) {
            return;
        }

        PlayerEntity player = event.getPlayer();
        if (!capability.hasPlayerSeenWelcome(player.getUniqueID())) {
            String wikiURL = "http://oitsjustjose.com/Mods/V-Tweaks/";
            Style style = Style.field_240709_b_;
            style = style.func_240718_a_(Color.func_240744_a_(TextFormatting.GREEN));
            style = style.func_240713_a_(true);
            style = style.func_244282_c(true);
            style = style.func_240715_a_(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiURL));
            style = style.func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_,
                    new TranslationTextComponent("vtweaks.intro.link")));

            player.sendMessage(new TranslationTextComponent("vtweaks.intro.message"), UUID.randomUUID());
            player.sendMessage(new TranslationTextComponent("vtweaks.intro.link").func_230530_a_(style),
                    UUID.randomUUID());
            capability.setPlayerSeenWelcome(player.getUniqueID());
        }
    }
}