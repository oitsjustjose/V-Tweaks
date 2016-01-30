package com.oitsjustjose.vtweaks.event.itemtweaks;

import java.util.List;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GamePlayHandler
{
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new GamePlayHandler());

		removeRecipe(new ItemStack(Items.stone_sword));
		removeRecipe(new ItemStack(Items.stone_pickaxe));
		removeRecipe(new ItemStack(Items.stone_shovel));
		removeRecipe(new ItemStack(Items.stone_axe));
		removeRecipe(new ItemStack(Items.stone_hoe));
		
		Items.wooden_sword.setMaxDamage(16);
		Items.wooden_pickaxe.setMaxDamage(16);
		Items.wooden_shovel.setMaxDamage(16);
		Items.wooden_axe.setMaxDamage(16);
		Items.wooden_hoe.setMaxDamage(16);
		
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
	
	@SubscribeEvent
	public void registerTweak(HarvestDropsEvent event)
	{
		if (event.harvester == null)
			return;
		if (event.isSilkTouching || Enchantments.hasAutoSmelt(event.harvester.getHeldItem()))
			return;

		if (event.state.getBlock() == Blocks.stone)
		{
			if (event.state.getBlock().getMetaFromState(event.state) == 0)
			{
				event.drops.clear();
				if (event.world.rand.nextInt(10 - event.fortuneLevel * 3) == 0)
					event.drops.add(new ItemStack(Items.flint));
				else
					event.drops.add(new ItemStack(Blocks.gravel));
			}
		}
	}
}