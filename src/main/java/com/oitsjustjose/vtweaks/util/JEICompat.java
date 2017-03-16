package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin
{

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
	}

	@Override
	public void register(IModRegistry registry)
	{
		if (VTweaks.config.enableRecipeHorseArmor)
		{
			addMirroredAnvilRecipe(registry, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_HORSE_ARMOR));
			addMirroredAnvilRecipe(registry, new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR));
			addMirroredAnvilRecipe(registry, new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		}
		if (VTweaks.config.hypermendingID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.NETHER_STAR), Enchantments.getEnchantedBook(Enchantments.hyperMending));
		}
		if (VTweaks.config.autosmeltID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.LAVA_BUCKET), Enchantments.getEnchantedBook(Enchantments.autosmelt));
		}
		// Stepboost Compatibility
		if (VTweaks.config.stepboostID > 0)
		{
			ArrayList<ItemStack> stairs = new ArrayList<ItemStack>();
			ArrayList<ItemStack> output = new ArrayList<ItemStack>();
			output.add(Enchantments.getEnchantedBook(Enchantments.stepboost));

			for (int i = 0; i < Block.REGISTRY.getKeys().size(); i++)
			{
				Block b = Block.REGISTRY.getObjectById(i);
				if (b != null)
				{
					if (b.getRegistryName().toString().contains("stair"))
					{
						stairs.add(new ItemStack(b, 16));
					}
				}
			}
			registry.addAnvilRecipe(new ItemStack(Items.WRITABLE_BOOK), stairs, output);
		}
		if (VTweaks.config.lumberingID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.GOLDEN_AXE), Enchantments.getEnchantedBook(Enchantments.lumbering));
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}

	void addMirroredAnvilRecipe(IModRegistry registry, ItemStack input, ItemStack output)
	{
		ArrayList<ItemStack> inputTemp = new ArrayList<ItemStack>();
		ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
		inputTemp.add(input);
		outputTemp.add(output);
		registry.addAnvilRecipe(input, inputTemp, outputTemp);
	}

	void addAnvilRecipe(IModRegistry registry, ItemStack inputLeft, ItemStack inputRight, ItemStack output)
	{
		ArrayList<ItemStack> rightTemp = new ArrayList<ItemStack>();
		ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
		rightTemp.add(inputRight);
		outputTemp.add(output);
		registry.addAnvilRecipe(inputLeft, rightTemp, outputTemp);
	}
}