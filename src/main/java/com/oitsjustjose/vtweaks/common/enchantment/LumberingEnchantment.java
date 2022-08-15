package com.oitsjustjose.vtweaks.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

public class LumberingEnchantment extends Enchantment {

    public LumberingEnchantment() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return this != enchantment;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.canPerformAction(ToolActions.AXE_DIG);
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