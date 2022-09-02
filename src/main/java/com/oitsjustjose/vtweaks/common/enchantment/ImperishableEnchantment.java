package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ImperishableEnchantment extends Enchantment {
    public ImperishableEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
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
