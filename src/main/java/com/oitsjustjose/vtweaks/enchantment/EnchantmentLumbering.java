package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class EnchantmentLumbering extends Enchantment
{
    protected EnchantmentLumbering()
    {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]
        { EntityEquipmentSlot.MAINHAND });
        this.setRegistryName(new ResourceLocation(VTweaks.MODID, "lumbering"));
        this.setName(new ResourceLocation(VTweaks.MODID, "lumbering").toString());
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        Set<String> classes = stack.getItem().getToolClasses(stack);
        return classes.contains("axe");
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