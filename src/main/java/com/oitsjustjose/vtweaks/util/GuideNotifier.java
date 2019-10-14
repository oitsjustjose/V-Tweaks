package com.oitsjustjose.vtweaks.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuideNotifier
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        PlayerEntity player = event.getPlayer();
        CompoundNBT tag = player.getPersistentData();
        if (!tag.contains("vtweaks:shown_wiki"))
        {
            Style style = new Style();
            String wikiURL = "http://oitsjustjose.com/Mods/V-Tweaks/";
            style.setColor(TextFormatting.GREEN);
            style.setBold(true);
            style.setUnderlined(true);
            style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, wikiURL));
            style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("vtweaks.intro.link")));

            player.sendMessage(new TranslationTextComponent( "vtweaks.intro.message"));
            player.sendMessage(new TranslationTextComponent("vtweaks.intro.link").setStyle(style));
            tag.putBoolean("vtweaks:shown_wiki", true);
        }
    }
}