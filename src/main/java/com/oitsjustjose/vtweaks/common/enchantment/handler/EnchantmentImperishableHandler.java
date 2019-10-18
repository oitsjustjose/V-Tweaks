package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.CombatRules;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantmentImperishableHandler
{
    // This event is for tools
    @SubscribeEvent
    public void register(BreakSpeed event)
    {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            return;
        }

        if (event.getEntity() == null || !(event.getEntity() instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntity();
        ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
        if (stack.isEmpty())
        {
            return;
        }
        if (stack.getItem() instanceof ToolItem)
        {
            ToolItem tool = (ToolItem) stack.getItem();

            if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
            {
                if (tool.getDamage(stack) >= (tool.getMaxDamage(stack) - 1))
                {
                    event.getPlayer().playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.25F,
                            (float) Math.min(1.0F, 0.5F + event.getPlayer().getRNG().nextDouble()));
                    event.setNewSpeed(0F);
                }
            }
        }
        else if (stack.getItem() instanceof SwordItem)
        {
            SwordItem sword = (SwordItem) stack.getItem();

            if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
            {
                if (sword.getDamage(stack) >= (sword.getMaxDamage(stack) - 1))
                {
                    event.getPlayer().playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.25F,
                            (float) Math.min(1.0F, 0.5F + event.getPlayer().getRNG().nextDouble()));
                    event.setNewSpeed(0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void register(AttackEntityEvent event)
    {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            return;
        }
        if (event.getPlayer() == null || event.getPlayer().isCreative())
        {
            return;
        }

        ItemStack stack = event.getPlayer().getHeldItemMainhand();

        if (stack.isEmpty() || !(stack.getItem() instanceof SwordItem)
                || EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) <= 0)
        {
            return;
        }

        SwordItem sword = (SwordItem) stack.getItem();

        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
        {
            if (sword.getDamage(stack) >= (sword.getMaxDamage(stack) - 1))
            {
                if (event.isCancelable())
                {
                    event.getPlayer().playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0F,
                            (float) Math.min(1.0F, 0.5F + event.getPlayer().getRNG().nextDouble()));
                    event.getPlayer().sendStatusMessage(new TranslationTextComponent("vtweaks.sword.damaged"), true);
                    event.setCanceled(true);
                }
            }
        }
    }

    // This event is for attacking / damage
    @SubscribeEvent
    public void register(LivingHurtEvent event)
    {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            return;
        }

        if (event.getEntityLiving() == null || event.getSource() == null)
        {
            return;
        }

        // Do nothing if the armor wouldn't be damaged in the first place
        if (event.getSource().isUnblockable())
        {
            return;
        }

        // For the case where an entity hurts a player
        if (event.getEntityLiving() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            float validDefense = 0.0F;
            float validToughness = 0.0F;

            for (ItemStack stack : player.getEquipmentAndArmor())
            {
                if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem))
                {
                    continue;
                }

                ArmorItem armor = (ArmorItem) stack.getItem();
                // How much the player is ACTUALLY getting hurt
                if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
                {
                    if (armor.getDamage(stack) >= (armor.getMaxDamage(stack) - 1))
                    {
                        player.setItemStackToSlot(armor.getEquipmentSlot(), stack.copy());
                    }
                    else
                    {
                        ServerPlayerEntity damager = null;
                        if (event.getSource().getTrueSource() instanceof ServerPlayerEntity)
                        {
                            damager = (ServerPlayerEntity) event.getSource().getTrueSource();
                        }
                        stack.attemptDamageItem(1, player.getRNG(), damager);
                    }
                }

                validDefense += armor.getDamageReduceAmount();
                validToughness += armor.getToughness();
            }

            float damage = CombatRules.getDamageAfterAbsorb(event.getAmount(), validDefense, validToughness);
            if (event.isCancelable())
            {
                VTweaks.proxy.hurt(player, player.getHealth() - damage);
                event.setCanceled(true);
            }
        }
    }
}