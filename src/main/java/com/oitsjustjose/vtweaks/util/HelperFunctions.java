package com.oitsjustjose.vtweaks.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HelperFunctions
{
	public static ItemStack getEnchantedBook(Enchantment ench)
	{
		ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
		Items.ENCHANTED_BOOK.addEnchantment(retBook, new EnchantmentData(ench, 1));
		return retBook;
	}

	/**
	 * @param modid
	 *            The mod owning the enchantment
	 * @param enchName
	 *            The internalized name of the enchantment
	 * @return The enchantment attached to the resource location
	 */
	public static Enchantment getEnchantment(String modid, String enchName)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(modid, enchName));
	}

	/**
	 * @param bookStack
	 *            ItemStack containing the enchantetd book
	 * @param enchantment
	 *            The enchantment being matched
	 * @return true if the book has said enchantment, false otherwise
	 */
	public static boolean bookHasEnchantment(ItemStack bookStack, Enchantment enchantment)
	{
		int id = Enchantment.getEnchantmentID(enchantment);

		if (bookStack.getItem() == Items.ENCHANTED_BOOK)
		{
			NBTTagCompound comp = bookStack.serializeNBT();
			if (comp != null)
			{
				comp = comp.getCompoundTag("tag");
				final NBTTagList list = comp.getTagList("StoredEnchantments", 10);
				for (int i = 0; i < list.tagCount(); i++)
				{
					comp = (NBTTagCompound) list.get(i);
					if (comp.getInteger("id") == id && comp.getInteger("lvl") > 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param bookStack
	 *            ItemStack containing the enchantetd book
	 * @param modid
	 *            The mod owning the enchantment
	 * @param enchName
	 *            The internalized name of the enchantment
	 * @return true if the book has said enchantment, false otherwise
	 */
	public static boolean bookHasEnchantment(ItemStack bookStack, String modid, String enchName)
	{
		int id = Enchantment.getEnchantmentID(Enchantment.REGISTRY.getObject(new ResourceLocation(modid, enchName)));

		if (bookStack.getItem() == Items.ENCHANTED_BOOK)
		{
			NBTTagCompound comp = bookStack.serializeNBT();
			if (comp != null)
			{
				comp = comp.getCompoundTag("tag");
				final NBTTagList list = comp.getTagList("StoredEnchantments", 10);
				for (int i = 0; i < list.tagCount(); i++)
				{
					comp = (NBTTagCompound) list.get(i);
					if (comp.getInteger("id") == id && comp.getInteger("lvl") > 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	// An easier builder function for Item Entities
	public static EntityItem createItemEntity(World world, BlockPos pos, ItemStack itemstack)
	{
		return new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemstack.copy());
	}

	// An easier builder function for Item Entities
	public static EntityItem createItemEntity(World world, BlockPos pos, Block block)
	{
		return new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(block));
	}

	// An easier builder function for Item Entities
	public static EntityItem createItemEntity(World world, BlockPos pos, Item item)
	{
		return new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
	}

	// Finds an itemstack using modid and name
	public static ItemStack findItemStack(String modid, String name)
	{
		ResourceLocation resLoc = new ResourceLocation(modid, name);
		if (Item.REGISTRY.containsKey(resLoc))
			return new ItemStack(Item.REGISTRY.getObject(resLoc));
		else if (Block.REGISTRY.containsKey(resLoc))
			if (Item.getItemFromBlock(Block.REGISTRY.getObject(resLoc)) != null)
				return new ItemStack(Item.getItemFromBlock(Block.REGISTRY.getObject(resLoc)), 1);

		return ItemStack.EMPTY;
	}
}
