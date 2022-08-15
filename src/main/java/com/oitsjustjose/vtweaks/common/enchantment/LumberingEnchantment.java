package com.oitsjustjose.vtweaks.common.enchantment;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

public class LumberingEnchantment extends Enchantment {
    private ItemStack cachedBook;

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

    public ItemStack asBook() {
        if (cachedBook == null) {
            this.cachedBook = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(this.cachedBook, new EnchantmentInstance(this, 1));
        }
        return this.cachedBook;
    }
}