package com.oitsjustjose.vtweaks.jei;

import java.util.ArrayList;
import java.util.List;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.HelperFunctions;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
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
		// The majority of the code below is not mine - it's based off of gigaherz's
		IJeiHelpers jeiHelp = registry.getJeiHelpers();
		IGuiHelper guiHelp = jeiHelp.getGuiHelper();

		registry.addRecipeCategories(new AnvilRecipeCategory(guiHelp));
		registry.addRecipeHandlers(new AnvilRecipeHandler());
		registry.addRecipeClickArea(GuiRepair.class, 102, 48, 22, 15, getUID());

		IRecipeTransferRegistry transReg = registry.getRecipeTransferRegistry();
		transReg.addRecipeTransferHandler(ContainerRepair.class, getUID(), 0, 2, 3, 36);

		registry.addRecipeCategoryCraftingItem(new ItemStack(Blocks.ANVIL), getUID());
		registerVTweaksRecipes(registry);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
	}

	private void addAnvilRecipe(IModRegistry registry, ItemStack inputLeft, ItemStack inputRight, ItemStack output, int xp)
	{
		ArrayList<ItemStack> rightTemp = new ArrayList<ItemStack>();
		ArrayList<AnvilRecipeWrapper> wrapTemp = new ArrayList<AnvilRecipeWrapper>();
		rightTemp.add(inputRight);
		wrapTemp.add(new AnvilRecipeWrapper(inputLeft, rightTemp, output, xp));
		registry.addRecipes(wrapTemp);
	}

	private void addAnvilRecipe(IModRegistry registry, ItemStack inputLeft, List<ItemStack> inputRight, ItemStack output, int xp)
	{
		ArrayList<AnvilRecipeWrapper> wrapTemp = new ArrayList<AnvilRecipeWrapper>();
		wrapTemp.add(new AnvilRecipeWrapper(inputLeft, inputRight, output, xp));
		registry.addRecipes(wrapTemp);
	}

	private void addAnvilRecipe(IModRegistry registry, ItemStack inputLeft, ItemStack output, int xp)
	{
		ArrayList<AnvilRecipeWrapper> wrapTemp = new ArrayList<AnvilRecipeWrapper>();
		wrapTemp.add(new AnvilRecipeWrapper(inputLeft, output, xp));
		registry.addRecipes(wrapTemp);
	}

	private void registerVTweaksRecipes(IModRegistry registry)
	{
		// Horse Armor Compatibility
		if (VTweaks.config.enableRecipeHorseArmor)
		{
			addAnvilRecipe(registry, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_HORSE_ARMOR), 4);
			addAnvilRecipe(registry, new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR), 8);
			addAnvilRecipe(registry, new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR), 12);
			addAnvilRecipe(registry, new ItemStack(Items.BED), new ItemStack(Items.BED).setStackDisplayName("Sleeping Bag"), 1);
		}
		// Hypermending Compatibility
		if (VTweaks.config.hypermendingID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.NETHER_STAR), HelperFunctions.getEnchantedBook(Enchantments.hypermending), VTweaks.config.hypermendingXPCost);
		}
		// Autosmelt Compatibility
		if (VTweaks.config.autosmeltID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.LAVA_BUCKET), HelperFunctions.getEnchantedBook(Enchantments.autosmelt), VTweaks.config.autosmeltXPCost);
		}
		// Stepboost Compatibility
		if (VTweaks.config.stepboostID > 0)
		{
			ArrayList<ItemStack> stairs = new ArrayList<ItemStack>();

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

			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), stairs, HelperFunctions.getEnchantedBook(Enchantments.stepboost), VTweaks.config.stepboostXPCost);
		}
		// Lumbering Compatibility
		if (VTweaks.config.lumberingID > 0)
		{
			addAnvilRecipe(registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.GOLDEN_AXE), HelperFunctions.getEnchantedBook(Enchantments.lumbering), VTweaks.config.lumberingXPCost);
		}
	}

	/**
	 * @return The UID string - MAY VARY depending if Toolbelt is loaded or not
	 */
	public static String getUID()
	{
		return VTweaks.MODID + ".anvil";
	}
}