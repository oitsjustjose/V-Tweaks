package com.oitsjustjose.vtweaks.common.util;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.world.capability.IVTweaksCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuideNotifier
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!CommonConfig.ENABLE_WELCOME_MESSAGE.get())
        {
            return;
        }

        IVTweaksCapability capability = event.getPlayer().getEntityWorld().getCapability(VTweaks.VTWEAKS_CAPABILITY)
                .orElse(null);

        if (capability == null)
        {
            return;
        }

        PlayerEntity player = event.getPlayer();
        if (!capability.hasPlayerSeenWelcome(player.getUniqueID()))
        {
            Style style = new Style();
            String wikiURL = "http://oitsjustjose.com/Mods/V-Tweaks/";
            style.setColor(TextFormatting.GREEN);
            style.setBold(true);
            style.setUnderlined(true);
            style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiURL));
            style.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("vtweaks.intro.link")));

            player.sendMessage(new TranslationTextComponent("vtweaks.intro.message"));
            player.sendMessage(new TranslationTextComponent("vtweaks.intro.link").setStyle(style));
            capability.setPlayerSeenWelcome(player.getUniqueID());
        }
    }
}