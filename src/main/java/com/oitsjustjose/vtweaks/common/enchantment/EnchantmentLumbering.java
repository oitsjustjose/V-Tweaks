package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class EnchantmentLumbering extends Enchantment {
    public EnchantmentLumbering() {
        super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        this.setRegistryName(new ResourceLocation(Constants.MODID, "lumbering"));
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
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