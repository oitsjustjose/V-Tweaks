package com.oitsjustjose.vtweaks.common.tweaks.player;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = LivingDropsEvent.class, category = "player")
public class CorpseDropsTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Prevents any drops dropped by the player on death from despawning").define("enablePlayerDeathDropSafety", true);
    }

    @Override
    public void process(Event event) {
        if (!this.enabled.get()) return;
        var evt = (LivingDropsEvent) event;
        if (!(evt.getEntity() instanceof Player)) return;

        evt.getDrops().forEach(ItemEntity::setUnlimitedLifetime);
    }
}
