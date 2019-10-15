package com.oitsjustjose.vtweaks.common.enchantment;

import java.util.Set;

import com.oitsjustjose.vtweaks.common.util.Constants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

public class EnchantmentLumbering extends Enchantment
{
    public EnchantmentLumbering()
    {
        super(Rarity.VERY_RARE, EnchantmentType.DIGGER, new EquipmentSlotType[]
        { EquipmentSlotType.MAINHAND });
        this.setRegistryName(new ResourceLocation(Constants.MODID, "lumbering"));
        this.name = this.getRegistryName().toString();
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        Set<ToolType> classes = stack.getItem().getToolTypes(stack);
        return classes.contains(ToolType.AXE);
    }

    @Override
    public int getMinEnchantability(int par1)
    {
        return 0;
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return 0;
    }
}