package com.oitsjustjose.vtweaks.common.tweaks.player;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@Tweak(category = "player")
public class FeatherFallingTweak extends VTweak {
    private ModConfigSpec.BooleanValue enabled;
    private Holder<Enchantment> featherFallingHolder = null;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.enabled = builder.comment("Feather Falling IV or above redirects all fall damage the player's boots").define("enableFeatherFallTweak", true);
    }

    @SubscribeEvent
    public void process(LivingDamageEvent.Pre evt) {
        if (!this.enabled.get()) return;
        if (!evt.getSource().is(DamageTypes.FALL)) return;
        if (!(evt.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel lvl)) return;

        var boots = player.getInventory().getArmor(0);
        if (boots.isEmpty()) return;

        if (featherFallingHolder == null) {
            try {
                featherFallingHolder = lvl.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FEATHER_FALLING);
            } catch (IllegalStateException exception) {
                VTweaks.getInstance().LOGGER.error("Failed to get holder for the Feather Falling enchantment -- does feather falling exist?");
                return;
            }
        }

        if (boots.getEnchantmentLevel(featherFallingHolder) < 4) return;
        boots.hurtAndBreak((int) evt.getOriginalDamage(), player, EquipmentSlot.FEET);
        evt.setNewDamage(0.0F);
    }
}
