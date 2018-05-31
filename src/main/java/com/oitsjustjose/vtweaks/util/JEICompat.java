package com.oitsjustjose.vtweaks.util;

import com.google.common.collect.ImmutableList;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IVanillaRecipeFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
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

        if (ModConfig.misc.enableHorseArmorRecipes)
        {
            addAnvilRecipe(factory, registry, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_HORSE_ARMOR));
            addAnvilRecipe(factory, registry, new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR));
            addAnvilRecipe(factory, registry, new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        }
        if (ModConfig.enchantments.enableLumbering)
        {
            addAnvilRecipe(factory, registry, new ItemStack(Items.WRITABLE_BOOK), new ItemStack(Items.GOLDEN_AXE), HelperFunctions.getEnchantedBook(Enchantments.getInstance().lumbering));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }


    private void addAnvilRecipe(IVanillaRecipeFactory factory, IModRegistry registry, ItemStack inputLeft, ItemStack inputRight, ItemStack output)
    {
        ArrayList<ItemStack> rightTemp = new ArrayList<ItemStack>();
        ArrayList<ItemStack> outputTemp = new ArrayList<ItemStack>();
        rightTemp.add(inputRight);
        outputTemp.add(output);
        registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(inputLeft, rightTemp, outputTemp)), VanillaRecipeCategoryUid.ANVIL);
    }
}