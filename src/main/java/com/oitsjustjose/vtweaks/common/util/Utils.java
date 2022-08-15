package com.oitsjustjose.vtweaks.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;

public class Utils {
    /**
     * @param bookStack ItemStack containing the enchanted book
     * @param target    The enchantment being matched
     * @param level     The level of the enchantment - can be null if level isn't of concern
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, Enchantment target, @Nullable Integer level) {
        Map<Enchantment, Integer> enchantmentsOnBook = EnchantmentHelper.getEnchantments(bookStack);
        for (Map.Entry<Enchantment, Integer> e : enchantmentsOnBook.entrySet()) {
            ResourceLocation rl1 = ForgeRegistries.ENCHANTMENTS.getKey(e.getKey());
            ResourceLocation rl2 = ForgeRegistries.ENCHANTMENTS.getKey(target);
            assert rl1 != null && rl2 != null;
            if (rl1.equals(rl2)) {
                if (level == null) return true;
                return e.getValue().equals(level);
            }
        }
        return false;
    }

    public static ItemEntity createItemEntity(Level world, BlockPos pos, ItemStack itemstack) {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemstack.copy());
    }

    public static ItemEntity createItemEntity(Level world, BlockPos pos, Item item) {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
    }
}
