package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;

public class EnchantmentImperishableHandler {
    // Breaking a block with any tool
    @SubscribeEvent
    public void register(BreakSpeed event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.getEntity();
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.isEmpty()) {
            return;
        }

        if (EnchantmentHelper.getItemEnchantmentLevel(VTweaks.imperishable, stack) > 0) {
            if (stack.getDamageValue() >= (stack.getMaxDamage() - 1)) {
                event.getPlayer().playSound(SoundEvents.SHIELD_BREAK, 0.25F,
                        (float) Math.min(1.0F, 0.5F + event.getPlayer().getRandom().nextDouble()));
                event.setNewSpeed(0F);
            }
        }
    }

    // Attacking an entity with any tool
    @SubscribeEvent
    public void register(AttackEntityEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
            return;
        }
        if (event.getPlayer() == null || event.getPlayer().isCreative()) {
            return;
        }

        ItemStack stack = event.getPlayer().getMainHandItem();
        if (EnchantmentHelper.getItemEnchantmentLevel(VTweaks.imperishable, stack) > 0) {
            if (stack.getDamageValue() >= (stack.getMaxDamage() - 1)) {
                if (event.isCancelable()) {
                    event.getPlayer().playSound(SoundEvents.SHIELD_BREAK, 1.0F,
                            (float) Math.min(1.0F, 0.5F + event.getPlayer().getRandom().nextDouble()));
                    event.getPlayer().displayClientMessage(new TranslatableComponent("vtweaks.tool.damaged"), true);
                    event.setCanceled(true);
                }
            }
        }
    }

    // Item use checks (e.g. shearing a sheep, tilling grass, etc.)
    @SubscribeEvent
    public void register(PlayerInteractEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
            return;
        }
        if (event.getPlayer() == null || event.getPlayer().isCreative()) {
            return;
        }

        ItemStack stack = event.getPlayer().getMainHandItem();
        if (EnchantmentHelper.getItemEnchantmentLevel(VTweaks.imperishable, stack) > 0) {
            if (stack.getDamageValue() >= (stack.getMaxDamage() - 1)) {
                if (event.isCancelable()) {
                    event.getPlayer().playSound(SoundEvents.SHIELD_BREAK, 1.0F,
                            (float) Math.min(1.0F, 0.5F + event.getPlayer().getRandom().nextDouble()));
                    event.getPlayer().displayClientMessage(new TranslatableComponent("vtweaks.tool.damaged"), true);
                    event.setCanceled(true);
                }
            }
        }
    }

    // Armor damage checks
    @SubscribeEvent
    public void register(PlayerEvent event) {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
            return;
        }

        try {
            ArrayList<ItemStack> salvaged = Lists.newArrayList();
            for (ItemStack stack : event.getPlayer().getArmorSlots()) {
                if (stack.getItem() instanceof ArmorItem) {
                    ArmorItem armor = (ArmorItem) stack.getItem();
                    if (EnchantmentHelper.getItemEnchantmentLevel(VTweaks.imperishable, stack) > 0) {
                        if (armor.getDamage(stack) >= (armor.getMaxDamage(stack) - 1)) {
                            salvaged.add(stack);
                        }
                    }
                }
            }

            salvaged.forEach((stack) -> {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), stack.copy());
                event.getPlayer().setItemSlot(LivingEntity.getEquipmentSlotForItem(stack), ItemStack.EMPTY);
                event.getPlayer().getInventory().setChanged();
                VTweaks.proxy.playSound(event.getPlayer());
            });
        } catch (NullPointerException ignored) {
            return;
        }
    }
}