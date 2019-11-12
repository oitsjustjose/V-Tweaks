package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FeatherFallingTweak
{
    @SubscribeEvent
    public void registerTweak(LivingHurtEvent event)
    {
        // Check if enchantment is disabled
        if (!EnchantmentConfig.ENABLE_FF_TWEAK.get())
        {
            return;
        }
        // Ensures we're working on a player entity AND we're working with fall damage
        if (!(event.getEntity() instanceof PlayerEntity) || event.getSource() != DamageSource.FALL)
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntity();

        // Checks if boots are worn
        if (player.inventory.armorInventory.get(0).isEmpty())
        {
            return;
        }

        ItemStack boots = player.inventory.armorInventory.get(0);
        // Checks if FeatherFalling IV or higher is on the boots
        if (EnchantmentHelper.getEnchantmentLevel(Utils.getEnchantment("minecraft", "feather_falling"),
                boots) >= 4)
        {
            boots.damageItem((int) event.getAmount(), player, this::breakBoots);
            event.setAmount(0.0F);
        }
    }

    public void breakBoots(PlayerEntity player)
    {
        // Checks if boots are worn
        if (player.inventory.armorInventory.get(0).isEmpty())
        {
            return;
        }

        player.inventory.armorInventory.set(0, ItemStack.EMPTY);
    }
}