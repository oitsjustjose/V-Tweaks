package com.oitsjustjose.VTweaks.Util;

import java.util.List;

import com.oitsjustjose.VTweaks.Enchantments.Enchantments;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeList
{
	public static void registerRecipes()
	{
		// Registers the disenchanting paper feature if config enabled AND if
		// neither Botania nor ThaumicTinkerer are installed
		// This was done because of the SpellBinding Cloths added by both. Don't
		// want to undermine other mods.
		if (ConfigHandler.disenchant && !(Loader.isModLoaded("Botania") || Loader.isModLoaded("ThaumicTinkerer")))
			CraftingManager.getInstance().getRecipeList().add(new DisenchantRecipe(Items.paper));
		
		removeRecipe(new ItemStack(Items.cauldron));
		GameRegistry.addRecipe(new ItemStack(Blocks.cauldron), new Object[]
		{
		"# #", "# #", "###", '#', Items.iron_ingot
		});
	}

	private IRecipe findRecipe(InventoryCrafting crafting, World world)
	{
		for (IRecipe recipe : (List<IRecipe>) CraftingManager.getInstance().getRecipeList())
			if (recipe.matches(crafting, world))
				return recipe;
		return null;
	}

	@SubscribeEvent
	public void registerEvent(AnvilUpdateEvent event)
	{
		registerBookRecipes(event);

		// Only registers horse armor recipes if enabled via config
		if (ConfigHandler.horseArmor)
			registerHorseArmorRecipes(event);
	}

	// Does what it says
	public void registerHorseArmorRecipes(AnvilUpdateEvent event)
	{
		// Drops out if either side doesn't have an item
		if (event.left == null || event.right == null)
			return;

		// Gets the left and right items
		Item left = event.left.getItem();
		Item right = event.right.getItem();

		// Determines if the items are damaged
		boolean damaged = event.left.getItemDamage() > 0 || event.right.getItemDamage() > 0;

		// Adds diamond horse armor recipe
		if (left == Items.diamond_leggings && right == Items.diamond_leggings && !damaged)
		{
			// Sets the XP cost
			event.cost = 35;
			// Sets the output
			event.output = new ItemStack(Items.diamond_horse_armor).setStackDisplayName("Large Dog Suit");
		}
		// Adds golden horse armor recipe
		if (left == Items.golden_leggings && right == Items.golden_leggings && !damaged)
		{
			// Sets the XP cost
			event.cost = 30;
			// Sets the output
			event.output = new ItemStack(Items.golden_horse_armor).setStackDisplayName("Medium Dog Suit");
		}
		// Adds iron horse armor recipe
		if (left == Items.iron_leggings && right == Items.iron_leggings && !damaged)
		{
			// Sets the XP cost
			event.cost = 20;
			// Sets the output
			event.output = new ItemStack(Items.iron_horse_armor).setStackDisplayName("Small Dog Suit");
		}

	}

	// Adds recipes for my enchanted books
	public void registerBookRecipes(AnvilUpdateEvent event)
	{
		// Initializes an Enchanted Book with Unbreakable
		ItemStack unbreakableBook = new ItemStack(Items.enchanted_book, 1, 0);
		Items.enchanted_book.addEnchantment(unbreakableBook, new EnchantmentData(Enchantments.unbreakable, 1));

		// Initializes an Enchanted Book with AutoSmelt
		ItemStack autosmeltBook = new ItemStack(Items.enchanted_book, 1, 0);
		Items.enchanted_book.addEnchantment(autosmeltBook, new EnchantmentData(Enchantments.autosmelt, 1));

		// Determines what to make the AutoSmelt book with. If fire creepers are
		// disabled, you'll have no way to get Fire, so it defaults to lava
		// buckets instead.
		ItemStack lavaBucket = new ItemStack(Items.lava_bucket);

		// Drops out if either side doesn't have an item
		if (event.left == null || event.right == null)
			return;

		// Checks to see if there's a nether star in the right slot and a Book &
		// Quill in the left slot
		if (event.left.getItem() == Items.writable_book && event.right.getItem() == Items.nether_star
				&& ConfigHandler.unbreakableEnchantmentID > 0)
			// Makes sure the nether star stacksize is just 1. If I didn't make
			// this check, it'd give the OK to make the recipe but then eat all
			// of the nether stars in the stack
			if (event.right.stackSize == 1)
			{
				// Sets the XP cost
				event.cost = 40;
				// Sets the output
				event.output = unbreakableBook;
			}
			else
				event.output = null;

		// Checks to see if there's a "hot item" in the right slot and a Book &
		// Quill in the left slot
		if (event.left.getItem() == Items.writable_book && event.right.getItem() == lavaBucket.getItem()
				&& ConfigHandler.autosmeltEnchantmentID > 0)
			// Checks the right side stack size according to the "hot item"
			if (event.right.stackSize == 1)
			{
				// Sets the XP cost
				event.cost = 40;
				// Sets the output
				event.output = autosmeltBook;
			}
			else
				event.output = null;
	}

	public static void removeRecipe(ItemStack result)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i) != null)
			{
				IRecipe temp = recipes.get(i);

				IRecipe recipe = temp;
				ItemStack recipeResult = recipe.getRecipeOutput();

				if (ItemStack.areItemStacksEqual(result, recipeResult))
					recipes.remove(i--);
			}
		}
	}
}