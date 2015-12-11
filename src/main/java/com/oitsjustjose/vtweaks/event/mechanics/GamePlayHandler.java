package com.oitsjustjose.vtweaks.event.mechanics;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GamePlayHandler
{
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new StoneHandler());

		removeRecipe(new ItemStack(Items.stone_sword));
		removeRecipe(new ItemStack(Items.stone_pickaxe));
		removeRecipe(new ItemStack(Items.stone_shovel));
		removeRecipe(new ItemStack(Items.stone_axe));
		removeRecipe(new ItemStack(Items.stone_hoe));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_sword), "#", "#", "S", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_pickaxe), "###", " S ", " S ", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_shovel), "#", "S", "S", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_axe), "## ", "#S ", " S ", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_axe), "## ", "S# ", "S  ", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.stone_hoe), "##", "S ", "S ", '#', Items.flint, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.cobblestone, 2), "##", "##", '#', Blocks.gravel));

	}

	public static void removeRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i) != null)
			{
				ItemStack recipeResult = recipes.get(i).getRecipeOutput();

				if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
					recipes.remove(i--);
			}
		}
	}
}