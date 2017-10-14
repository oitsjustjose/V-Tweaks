package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentMultifaceted extends Enchantment
{
    public EnchantmentMultifaceted()
    {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(new ResourceLocation(VTweaks.MODID, "multifaceted"));
        this.setName(new ResourceLocation(VTweaks.MODID, "multifaceted").toString());
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment)
    {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof ItemPickaxe;
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
