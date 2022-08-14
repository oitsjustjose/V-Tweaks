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

import java.util.Map;

public class Utils {
    public static ItemStack getEnchantedBook(Enchantment ench) {
        ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(retBook, new EnchantmentInstance(ench, 1));
        return retBook;
    }

    public static ItemStack getEnchantedBook(Enchantment ench, int lvl) {
        ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(retBook, new EnchantmentInstance(ench, lvl));
        return retBook;
    }

    /**
     * @param modid    The mod owning the enchantment
     * @param enchName The internalized name of the enchantment
     * @return The enchantment attached to the resource location
     */
    public static Enchantment getEnchantment(String modid, String enchName) {
        return ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(modid, enchName));
    }

    /**
     * @param bookStack ItemStack containing the enchanted book
     * @param target    The enchantment being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, Enchantment target) {
        Map<Enchantment, Integer> enchantmentsOnBook = EnchantmentHelper.getEnchantments(bookStack);
        for (Enchantment enchantment : enchantmentsOnBook.keySet()) {
            ResourceLocation rl1 = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
            ResourceLocation rl2 = ForgeRegistries.ENCHANTMENTS.getKey(target);
            assert rl1 != null && rl2 != null;
            if (rl1.equals(rl2)) return true;
        }
        return false;
    }

    /**
     * @param bookStack           ItemStack containing the enchanted book
     * @param EnchantmentInstance The EnchantmentInstance being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, EnchantmentInstance target) {
        Map<Enchantment, Integer> enchantmentsOnBook = EnchantmentHelper.getEnchantments(bookStack);
        for (Enchantment enchantment : enchantmentsOnBook.keySet()) {
            ResourceLocation rl1 = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
            ResourceLocation rl2 = ForgeRegistries.ENCHANTMENTS.getKey(target.enchantment);
            assert rl1 != null && rl2 != null;
            if (rl1.equals(rl2)) {
                if (enchantmentsOnBook.get(enchantment) == target.level) return true;
            }
        }
        return false;
    }

    /**
     * @param enchantment The enchantment being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static CompoundTag getEnchantedBookNBT(Enchantment enchantment, int level) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level)).getTag();
    }

    /**
     * @param bookStack ItemStack containing the enchantetd book
     * @param modid     The mod owning the enchantment
     * @param enchName  The internalized name of the enchantment
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, String modid, String enchName) {
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(modid, enchName));
        if (enchantment == null) {
            return false;
        }
        return bookHasEnchantment(bookStack, enchantment);
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(Level world, BlockPos pos, ItemStack itemstack) {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemstack.copy());
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(Level world, BlockPos pos, Block block) {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block));
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(Level world, BlockPos pos, Item item) {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
    }

    // Finds an itemstack using modid and name
    public static ItemStack findItemStack(String modid, String name) {
        ResourceLocation resLoc = new ResourceLocation(modid, name);

        if (ForgeRegistries.ITEMS.containsKey(resLoc)) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(resLoc));
        }

        return ItemStack.EMPTY;
    }
}
