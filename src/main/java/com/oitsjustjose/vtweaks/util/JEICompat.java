package com.oitsjustjose.vtweaks.util;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
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
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IVanillaRecipeFactory factory = jeiHelpers.getVanillaRecipeFactory();
		
		if (VTweaks.config.enableRecipeHorseArmor)
		{
			addMirroredAnvilRecipe(factory, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_HORSE_ARMOR));
			addMirroredAnvilRecipe(factory, new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR));
			addMirroredAnvilRecipe(factory, new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR));
		}
		if (VTweaks.config.hypermendingID > 0)
		{
			addAnvilRecipe(factory, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.NETHER_STAR), HelperFunctions.getEnchantedBook(Enchantments.hypermending));
		}
		if (VTweaks.config.autosmeltID > 0)
		{
			addAnvilRecipe(factory, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.LAVA_BUCKET), HelperFunctions.getEnchantedBook(Enchantments.autosmelt));
		}
		// Stepboost Compatibility
		if (VTweaks.config.stepboostID > 0)
		{
			ArrayList<ItemStack> stairs = new ArrayList<ItemStack>();
			ArrayList<ItemStack> output = new ArrayList<ItemStack>();
			final ItemStack stepboostBook = HelperFunctions.getEnchantedBook(Enchantments.stepboost);
			for (int i = 0; i < Block.REGISTRY.getKeys().size(); i++)
			{
				Block b = Block.REGISTRY.getObjectById(i);
				if (b != null)
				{
					if (b.getRegistryName().toString().contains("stair"))
					{
						stairs.add(new ItemStack(b, 16));
						output.add(stepboostBook);
					}
				}
			}
			factory.createAnvilRecipe(new ItemStack(Items.WRITABLE_BOOK), stairs, output);
		}
		if (VTweaks.config.lumberingID > 0)
		{
			addAnvilRecipe(factory, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.GOLDEN_AXE), HelperFunctions.getEnchantedBook(Enchantments.lumbering));
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}

	void addMirroredAnvilRecipe(IVanillaRecipeFactory factory, ItemStack input, ItemStack output)
	{
		ArrayList<ItemStack> inputTemp = new ArrayList<ItemStack>();
		ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
		inputTemp.add(input);
		outputTemp.add(output);
		factory.createAnvilRecipe(input, inputTemp, outputTemp);
	}

	void addAnvilRecipe(IVanillaRecipeFactory factory, ItemStack inputLeft, ItemStack inputRight, ItemStack output)
	{
		ArrayList<ItemStack> rightTemp = new ArrayList<ItemStack>();
		ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
		rightTemp.add(inputRight);
		outputTemp.add(output);
		factory.createAnvilRecipe(inputLeft, rightTemp, outputTemp);
	}
}