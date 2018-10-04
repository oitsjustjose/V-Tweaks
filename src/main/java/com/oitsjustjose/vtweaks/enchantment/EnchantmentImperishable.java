package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

public class EnchantmentImperishable extends Enchantment
{
    protected EnchantmentImperishable()
    {
        super(Rarity.UNCOMMON, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.values());
        this.setRegistryName(new ResourceLocation(VTweaks.MODID, "imperishable"));
        this.setName(new ResourceLocation(VTweaks.MODID, "imperishable").toString());
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return true;
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
