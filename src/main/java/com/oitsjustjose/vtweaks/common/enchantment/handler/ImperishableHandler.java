package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;

public class ImperishableHandler {
    private final TranslatableContents notification = new TranslatableContents("vtweaks.tool.damaged");

    @SubscribeEvent
    public void register(BreakSpeed event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) return;

        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.isEmpty()) return;

        if (stack.getEnchantmentLevel(VTweaks.Enchantments.imperishable) > 0) {
            if (stack.getDamageValue() >= (stack.getMaxDamage() - 1)) {
                player.playSound(SoundEvents.SHIELD_BREAK, 0.25F, (float) Math.min(1.0F, 0.5F + player.getRandom().nextDouble()));
                event.setNewSpeed(0F);
            }
        }
    }

    private void notifyPlayer(ServerPlayer player) {
        player.playSound(SoundEvents.SHIELD_BREAK, 1.0F, (float) Math.min(1.0F, 0.5F + player.getRandom().nextDouble()));
        MutableComponent component;
        try {
            component = notification.resolve(null, player, 0);
        } catch (CommandSyntaxException e) {
            component = Component.empty().append(e.getMessage());
            e.printStackTrace();
        }
        player.displayClientMessage(component, true);
    }

    /* Code dedup effort */
    public boolean handle(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.getEnchantmentLevel(VTweaks.Enchantments.imperishable) > 0) {
            if (stack.getDamageValue() >= (stack.getMaxDamage() - 1)) {
                notifyPlayer(player);
                return true;
            }
        }
        return false;
    }


    // Attacking an entity with any tool
    @SubscribeEvent
    public void register(AttackEntityEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) return;
        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (event.isCancelable() && handle(player)) {
            event.setCanceled(true);
        }
    }

    // Item use checks (e.g. shearing a sheep, tilling grass, etc.)
    @SubscribeEvent
    public void register(PlayerInteractEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) return;
        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (event.isCancelable() && handle(player)) {
            event.setCanceled(true);
        }
    }

    // Armor damage checks
    @SubscribeEvent
    public void register(PlayerEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) return;
        if (event.getEntity() == null) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        try {
            ArrayList<ItemStack> salvaged = Lists.newArrayList();
            for (ItemStack stack : player.getArmorSlots()) {
                if (stack.getItem() instanceof ArmorItem armor) {
                    if (stack.getEnchantmentLevel(VTweaks.Enchantments.imperishable) > 0) {
                        if (armor.getDamage(stack) >= (armor.getMaxDamage(stack) - 1)) {
                            salvaged.add(stack);
                        }
                    }
                }
            }

            salvaged.forEach((stack) -> {
                ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
                player.setItemSlot(LivingEntity.getEquipmentSlotForItem(stack), ItemStack.EMPTY);
                player.getInventory().setChanged();
                VTweaks.proxy.playSound(event.getEntity());
            });
        } catch (NullPointerException ignored) {}
    }
}