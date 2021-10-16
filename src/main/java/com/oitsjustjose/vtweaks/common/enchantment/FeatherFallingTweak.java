package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FeatherFallingTweak {
    @SubscribeEvent
    public void registerTweak(LivingHurtEvent event) {
        // Check if enchantment is disabled
        if (!EnchantmentConfig.ENABLE_FF_TWEAK.get()) {
            return;
        }
        // Ensures we're working on a player entity AND we're working with fall damage
        if (!(event.getEntity() instanceof ServerPlayer) || event.getSource() != DamageSource.FALL) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ItemStack boots = player.getInventory().getArmor(0);
        // Checks if boots are worn
        if (boots.isEmpty()) {
            return;
        }

        // Checks if FeatherFalling IV or higher is on the boots
        if (EnchantmentHelper.getItemEnchantmentLevel(Utils.getEnchantment("minecraft", "feather_falling"), boots) >= 4) {
            boots.hurt((int) event.getAmount(), player.getRandom(), player);
            event.setAmount(0.0F);
        }
    }
}