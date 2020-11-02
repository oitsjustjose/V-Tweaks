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
            Style style = Style.EMPTY;
            style = style.setColor(Color.fromTextFormatting(TextFormatting.GREEN));
            style = style.setUnderlined(true);
            style = style.setBold(true);
            style = style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiURL));
            style = style.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("vtweaks.intro.link")));

            player.sendMessage(new TranslationTextComponent("vtweaks.intro.message"), UUID.randomUUID());
            player.sendMessage(new TranslationTextComponent("vtweaks.intro.link").setStyle(style), UUID.randomUUID());
            capability.setPlayerSeenWelcome(player.getUniqueID());
        }
    }
}