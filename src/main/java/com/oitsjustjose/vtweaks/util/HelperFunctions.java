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
	// Makes creating itemstacks one-liners!
	public static ItemStack getEnchantedBook(Enchantment ench)
	{
		ItemStack retBook = new ItemStack(Items.ENCHANTED_BOOK);
		Items.ENCHANTED_BOOK.addEnchantment(retBook, new EnchantmentData(ench, 1));
		return retBook;
	}

	// A little easier than remembering every sub-call each time
	public static Enchantment getEnchantment(String modid, String enchName)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(modid, enchName));
	}

	// Checks if the enchanted book passed is storing an enchantment
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
	 * @param enchantment
	 *            The enchantment being matched
	 * @return true if the book has said enchantment, false otherwise
	 */
	public static NBTTagCompound getEnchantedBookNBT(Enchantment enchantment, int level)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("StoredEnchantments", new NBTTagList());

		NBTTagList nbttaglist = tag.getTagList("StoredEnchantments", 10);
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(enchantment));
		nbttagcompound.setShort("lvl", (short) level);
		nbttaglist.appendTag(nbttagcompound);

		return tag;
	}

	// Same as before but with simplified form
	public static boolean bookHasEnchantment(ItemStack bookStack, String modid, String enchName)
	{
		int id = Enchantment.getEnchantmentID(getEnchantment(modid, enchName));

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

		return null;
	}
}
