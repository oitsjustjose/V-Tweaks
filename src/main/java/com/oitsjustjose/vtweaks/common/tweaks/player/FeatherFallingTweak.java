package com.oitsjustjose.vtweaks.common.tweaks.player;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = LivingHurtEvent.class, category = "player")
public class FeatherFallingTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Feather Falling IV or above directs 100% of fall damage to boots").define("enableFeatherFallTweak", true);
    }

    @SubscribeEvent
    public void process(LivingHurtEvent evt) {
        if (!this.enabled.get()) return;
        if (evt.getSource() != DamageSource.FALL) return;
        if (!(evt.getEntity() instanceof ServerPlayer player)) return;

        var boots = player.getInventory().getArmor(0);
        if (boots.isEmpty()) return;

        if (boots.getEnchantmentLevel(Enchantments.FALL_PROTECTION) < 4) return;

        if(boots.hurt((int) evt.getAmount(), player.getRandom(), player)) {
            boots.shrink(1);
        }
        evt.setAmount(0.0F);
    }
}
