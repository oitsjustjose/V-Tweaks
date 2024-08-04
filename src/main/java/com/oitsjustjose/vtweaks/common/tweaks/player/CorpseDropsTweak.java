package com.oitsjustjose.vtweaks.common.tweaks.player;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@Tweak(category = "player")
public class CorpseDropsTweak extends VTweak {
    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Prevents any drops dropped by the player on death from despawning").define("enablePlayerDeathDropSafety", true);
    }

    @SubscribeEvent
    public void process(LivingDropsEvent evt) {
        if (!this.enabled.get()) return;
        if (!(evt.getEntity() instanceof Player)) return;

        evt.getDrops().forEach(ItemEntity::setUnlimitedLifetime);
    }
}
