package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.util.Constants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public class EnchantmentImperishable extends Enchantment {
    public EnchantmentImperishable() {
        super(Rarity.UNCOMMON, EnchantmentType.BREAKABLE, EquipmentSlotType.values());
        this.setRegistryName(new ResourceLocation(Constants.MODID, "imperishable"));
        this.name = this.getRegistryName().toString();
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return this != enchantment;
    }

    @Override
    public int getMinEnchantability(int par1) {
        return 0;
    }

    @Override
    public int getMaxEnchantability(int par1) {
        return 0;
    }
}
