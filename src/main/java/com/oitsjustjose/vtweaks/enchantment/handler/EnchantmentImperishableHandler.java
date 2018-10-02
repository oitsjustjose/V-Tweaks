package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.HelperFunctions;
import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetNBT;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentImperishableHandler
{
    // This event is for tools
    @SubscribeEvent
    public void register(PlayerInteractEvent event)
    {
        if (!ModConfig.enchantments.enableImperishable)
        {
            return;
        }

        if (event.getItemStack().isEmpty() || event.getEntityPlayer() == null)
        {
            return;
        }

        ItemStack stack = event.getItemStack();

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().imperishable, stack) > 0)
        {
            if (stack.getItemDamage() >= stack.getMaxDamage())
            {
                // Assuming it's a bed item named "Sleeping Bag":
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
        if (!ModConfig.enchantments.enableImperishable)
        {
            return;
        }

        if (event.getEntityLiving() == null || event.getSource() == null)
        {
            return;
        }
        // For the case where a player hurts an entity
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            ItemStack stack = player.getHeldItemMainhand();

            if (stack.isEmpty())
            {
                return;
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().imperishable, stack) > 0)
            {
                if (stack.getItemDamage() >= stack.getMaxDamage())
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
        else if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();

            float validDefense = 0.0F;
            float validToughness = 0.0F;
            for (ItemStack stack : player.getEquipmentAndArmor())
            {

                if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmor))
                {
                    continue;
                }

                // How much the player is ACTUALLY getting hurt
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().imperishable, stack) > 0)
                {
                    if (stack.getItemDamage() >= stack.getMaxDamage())
                    {
                        stack.attemptDamageItem(-1, player.getRNG(), null);
                        continue;
                    }
                }

                ItemArmor armor = (ItemArmor) stack.getItem();
                validDefense += armor.damageReduceAmount;
                validToughness += armor.toughness;
            }

            float damage = CombatRules.getDamageAfterAbsorb(event.getAmount(), validDefense, validToughness);
            event.setAmount(damage);
        }
    }

    // This event is for the generation of teh lewtz
    @SubscribeEvent
    public void lootLoad(LootTableLoadEvent event)
    {
        if (!ModConfig.enchantments.enableImperishable)
        {
            return;
        }

        LootCondition[] none = new LootCondition[0];
        LootPool pool = event.getTable().getPool("main");

        if (LootTableList.CHESTS_NETHER_BRIDGE.equals(event.getName()))
        {
            LootFunction enchantment = new SetNBT(none,
                    HelperFunctions.getEnchantedBookNBT(Enchantments.getInstance().imperishable, 1));
            LootFunction quantity = new SetCount(none, new RandomValueRange(1));
            pool.addEntry(new LootEntryItem(Items.ENCHANTED_BOOK, 20, 0, new LootFunction[]
            { enchantment, quantity }, none, VTweaks.MODID + ":imperishable_book"));
        }
    }
}
