package com.oitsjustjose.vtweaks.util;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class HelperFunctions
{
    public static ItemStack getEnchantedBook(Enchantment ench)
    {
        ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(retBook, new EnchantmentData(ench, 1));
        return retBook;
    }

    public static ItemStack getEnchantedBook(Enchantment ench, int lvl)
    {
        ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(retBook, new EnchantmentData(ench, lvl));
        return retBook;
    }

    /**
     * @param modid    The mod owning the enchantment
     * @param enchName The internalized name of the enchantment
     * @return The enchantment attached to the resource location
     */
    public static Enchantment getEnchantment(String modid, String enchName)
    {
        return ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(modid, enchName));
    }

    /**
     * @param bookStack   ItemStack containing the enchantetd book
     * @param enchantment The enchantment being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, Enchantment enchantment)
    {
        Map<Enchantment, Integer> bookEnchs = EnchantmentHelper.getEnchantments(bookStack);
        for (Enchantment ench : bookEnchs.keySet())
        {
            if (ench.getName().equalsIgnoreCase(enchantment.getName()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * @param bookStack       ItemStack containing the enchantetd book
     * @param enchantmentData The enchantmentdata being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, EnchantmentData enchantmentData)
    {
        Map<Enchantment, Integer> bookEnchs = EnchantmentHelper.getEnchantments(bookStack);
        for (Enchantment ench : bookEnchs.keySet())
        {
            if (ench.getName().equalsIgnoreCase(enchantmentData.enchantment.getName()))
            {
                if (bookEnchs.get(ench) == enchantmentData.enchantmentLevel)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param bookStack   ItemStack containing the enchantetd book
     * @param enchantment The enchantment being matched
     * @return true if the book has said enchantment, false otherwise
     */
    public static CompoundNBT getEnchantedBookNBT(Enchantment enchantment, int level)
    {
        return EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, level)).getTag();
    }

    /**
     * @param bookStack ItemStack containing the enchantetd book
     * @param modid     The mod owning the enchantment
     * @param enchName  The internalized name of the enchantment
     * @return true if the book has said enchantment, false otherwise
     */
    public static boolean bookHasEnchantment(ItemStack bookStack, String modid, String enchName)
    {
        Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(modid, enchName));
        if (enchantment == null)
        {
            return false;
        }
        return bookHasEnchantment(bookStack, enchantment);
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(World world, BlockPos pos, ItemStack itemstack)
    {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemstack.copy());
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(World world, BlockPos pos, Block block)
    {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block));
    }

    // An easier builder function for Item Entities
    public static ItemEntity createItemEntity(World world, BlockPos pos, Item item)
    {
        return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
    }

    // Finds an itemstack using modid and name
    public static ItemStack findItemStack(String modid, String name)
    {
        ResourceLocation resLoc = new ResourceLocation(modid, name);

        if (ForgeRegistries.ITEMS.containsKey(resLoc))
        {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(resLoc));
        }

        return ItemStack.EMPTY;
    }
}
