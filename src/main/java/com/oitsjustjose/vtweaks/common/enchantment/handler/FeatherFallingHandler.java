package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FeatherFallingHandler {
    @SubscribeEvent
    public void registerTweak(LivingHurtEvent event) {
        if (!EnchantmentConfig.ENABLE_FF_TWEAK.get()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getSource() != DamageSource.FALL) return;


        ItemStack boots = player.getInventory().getArmor(0);
        // Checks if boots are worn
        if (boots.isEmpty()) return;

        // Checks if FeatherFalling IV or higher is on the boots
        if (boots.getEnchantmentLevel(Enchantments.FALL_PROTECTION) >= 4) {
            boots.hurt((int) event.getAmount(), player.getRandom(), player);
            event.setAmount(0.0F);
        }
    }
}