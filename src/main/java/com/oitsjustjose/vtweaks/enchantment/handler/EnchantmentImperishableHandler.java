package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.config.EnchantmentConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.CombatRules;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantmentImperishableHandler
{
    // This event is for tools
    @SubscribeEvent
    public void register(PlayerInteractEvent event)
    {
        if (!EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            return;
        }

        if (event.getItemStack().isEmpty() || event.getEntity() == null || !(event.getEntity() instanceof PlayerEntity))
        {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ToolItem))
        {
            return;
        }
        ToolItem tool = (ToolItem) stack.getItem();

        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
        {
            if (tool.getDamage(stack) >= tool.getMaxDamage(stack))
            {
                if (event.isCancelable())
                {
                    event.setCanceled(true);
                }
                event.setResult(Result.DENY);
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
        // For the case where a player hurts an entity
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            ItemStack stack = player.getHeldItemMainhand();

            if (stack.isEmpty() || !(stack.getItem() instanceof ToolItem))
            {
                return;
            }

            ToolItem tool = (ToolItem) stack.getItem();

            if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, stack) > 0)
            {
                if (tool.getDamage(stack) >= tool.getMaxDamage(stack))
                {
                    stack.attemptDamageItem(-1, player.getRNG(), null);

                    if (event.isCancelable())
                    {
                        event.setCanceled(true);
                    }
                    event.setResult(Result.DENY);
                    event.setAmount(0.0F);
                }
            }
        }
        // For the case where an entity hurts a player
        else if (event.getEntityLiving() instanceof PlayerEntity)
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
                        stack.attemptDamageItem(-1, player.getRNG(), null);
                        continue;
                    }
                }

                validDefense += armor.getDamageReduceAmount();
                validToughness += armor.getToughness();
            }

            float damage = CombatRules.getDamageAfterAbsorb(event.getAmount(), validDefense, validToughness);
            event.setAmount(damage);
        }
    }
}