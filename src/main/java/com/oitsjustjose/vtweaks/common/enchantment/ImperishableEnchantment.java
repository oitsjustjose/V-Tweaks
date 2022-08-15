package com.oitsjustjose.vtweaks.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class ImperishableEnchantment extends Enchantment {

    public ImperishableEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return this != enchantment;
    }

    @Override
    public int getMinCost(int par1) {
        return 0;
    }

    @Override
    public int getMaxCost(int par1) {
        return 0;
    }
}
