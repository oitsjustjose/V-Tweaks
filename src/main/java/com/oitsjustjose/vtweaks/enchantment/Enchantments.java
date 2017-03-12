package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

public class Enchantments
{
	public static Enchantment hyperMending;
	public static Enchantment autosmelt;
	public static Enchantment stepboost;
	public static Enchantment lumbering;

	public static void initialize()
	{
		if (VTweaks.config.hypermendingID != 0)
		{
			hyperMending = new EnchantmentHypermending().setName(VTweaks.MODID + ":hypermending");
		}

		if (VTweaks.config.autosmeltID != 0)
		{
			autosmelt = new EnchantmentAutosmelt().setName(VTweaks.MODID + ":autosmelt");
		}

		if (VTweaks.config.stepboostID != 0)
		{
			stepboost = new EnchantmentStepboost().setName(VTweaks.MODID + ":stepboost");
		}

		if (VTweaks.config.lumberingID != 0)
		{
			lumbering = new EnchantmentLumbering().setName(VTweaks.MODID + ":lumbering");
		}
	}

	/**
	 * @param enchName
	 *            The string version of the Resource Location
	 * @return The enchantment attached to the resource location
	 */
	public static Enchantment getEnchantment(String enchName)
	{
		return Enchantment.REGISTRY.getObject(new ResourceLocation(enchName));
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
	 * @param enchName
	 *            The internalized name of the enchantment
	 * @return true if the book has said enchantment, false otherwise
	 */
	public static boolean bookHasEnchantment(ItemStack bookStack, String enchName)
	{
		int id = Enchantment.getEnchantmentID(Enchantment.REGISTRY.getObject(new ResourceLocation(enchName)));

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
}