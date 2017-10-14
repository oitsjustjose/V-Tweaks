package com.oitsjustjose.vtweaks.util;

import com.google.common.collect.ImmutableList;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

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

        if (Config.getInstance().enableRecipeHorseArmor)
        {
            addMirroredAnvilRecipe(factory, registry, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_HORSE_ARMOR));
            addMirroredAnvilRecipe(factory, registry, new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR));
            addMirroredAnvilRecipe(factory, registry, new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        }
        if (Config.getInstance().enableEnchHypermending)
        {
            addAnvilRecipe(factory, registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.NETHER_STAR), HelperFunctions.getEnchantedBook(Enchantments.getInstance().hypermending));
        }
        if (Config.getInstance().enableEnchAutosmelt)
        {
            addAnvilRecipe(factory, registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.LAVA_BUCKET), HelperFunctions.getEnchantedBook(Enchantments.getInstance().autosmelt));
        }
        // Stepboost Compatibility
        if (Config.getInstance().enableEnchStepboost)
        {
            ArrayList<ItemStack> stairs = new ArrayList<ItemStack>();
            ArrayList<ItemStack> output = new ArrayList<ItemStack>();
            final ItemStack stepboostBook = HelperFunctions.getEnchantedBook(Enchantments.getInstance().stepboost);
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
            registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(new ItemStack(Items.WRITABLE_BOOK), stairs, output)), VanillaRecipeCategoryUid.ANVIL);
        }
        if (Config.getInstance().enableEnchLumbering)
        {
            addAnvilRecipe(factory, registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.GOLDEN_AXE), HelperFunctions.getEnchantedBook(Enchantments.getInstance().lumbering));
        }
        if (Config.getInstance().enableEnchMultifaceted)
        {
            registry.addIngredientInfo(HelperFunctions.getEnchantedBook(Enchantments.getInstance().multifaceted), ItemStack.class, "recipe.multifaceted.name");
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }

    void addMirroredAnvilRecipe(IVanillaRecipeFactory factory, IModRegistry registry, ItemStack input, ItemStack output)
    {
        ArrayList<ItemStack> inputTemp = new ArrayList<ItemStack>();
        ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
        inputTemp.add(input);
        outputTemp.add(output);
        registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(input, inputTemp, outputTemp)), VanillaRecipeCategoryUid.ANVIL);
    }

    void addAnvilRecipe(IVanillaRecipeFactory factory, IModRegistry registry, ItemStack inputLeft, ItemStack inputRight, ItemStack output)
    {
        ArrayList<ItemStack> rightTemp = new ArrayList<ItemStack>();
        ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
        rightTemp.add(inputRight);
        outputTemp.add(output);
        registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(inputLeft, rightTemp, outputTemp)), VanillaRecipeCategoryUid.ANVIL);
    }
}