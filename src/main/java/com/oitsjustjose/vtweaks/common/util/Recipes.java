package com.oitsjustjose.vtweaks.common.util;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Recipes
{
    @SubscribeEvent
    public void registerBookRecipes(AnvilUpdateEvent event)
    {
        if (EnchantmentConfig.ENABLE_LUMBERING.get())
        {
            ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.lumbering);

            if (event.getLeft().isEmpty() || event.getRight().isEmpty())
                return;

            boolean damaged = event.getRight().getDamage() > 0;

            if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.GOLDEN_AXE)
            {
                if (!damaged)
                {
                    event.setCost(EnchantmentConfig.LUMBERING_RECIPE_COST.get());
                    event.setOutput(book);
                }
                else
                {
                    event.setOutput(ItemStack.EMPTY);
                }
            }
        }

        if (EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            ItemStack book = HelperFunctions.getEnchantedBook(VTweaks.imperishable);

            if (event.getLeft().isEmpty() || event.getRight().isEmpty())
                return;

            if (event.getLeft().getItem() == Items.ENCHANTED_BOOK && event.getRight().getItem() == Items.ENCHANTED_BOOK)
            {
                if (HelperFunctions.bookHasEnchantment(event.getLeft(), new EnchantmentData(Enchantments.UNBREAKING, 3))
                        && HelperFunctions.bookHasEnchantment(event.getRight(),
                                new EnchantmentData(Enchantments.UNBREAKING, 3)))
                {
                    event.setCost(EnchantmentConfig.IMPERISHABLE_RECIPE_COST.get());
                    event.setOutput(book);
                }
            }
        }
    }
}
