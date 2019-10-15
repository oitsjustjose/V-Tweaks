package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.config.EnchantmentConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.CombatRules;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;

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
                if (tool.getDamage(stack) >= tool.getMaxDamage(stack))
                {
                    event.setNewSpeed(0F);
                }
            }
        }
        else if (stack.getItem() instanceof SwordItem)
        {
            SwordItem sword = (SwordItem) stack.getItem();

            if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
            {
                if (sword.getDamage(stack) >= sword.getMaxDamage(stack))
                {
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
            if (sword.getDamage(stack) >= sword.getMaxDamage(stack))
            {
                if (event.isCancelable())
                {
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
                    if (armor.getDamage(stack) >= armor.getMaxDamage(stack))
                    {
                        player.setItemStackToSlot(armor.getEquipmentSlot(), stack.copy());
                        continue;
                    }
                }

                validDefense += armor.getDamageReduceAmount();
                validToughness += armor.getToughness();
            }

            float damage = CombatRules.getDamageAfterAbsorb(event.getAmount(), validDefense, validToughness);
            if (event.isCancelable())
            {
                player.setHealth(player.getHealth() - damage);
                player.performHurtAnimation();
                event.setCanceled(true);
            }
        }
    }
}