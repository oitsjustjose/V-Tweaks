package com.oitsjustjose.vtweaks.common.util;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompat implements IModPlugin
{
    private static ResourceLocation ID = new ResourceLocation(Constants.MODID, "ench_books");

    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        ArrayList<Object> recipes = Lists.newArrayList();
        recipes.add(registration.getVanillaRecipeFactory().createAnvilRecipe(new ItemStack(Items.WRITABLE_BOOK),
                Lists.newArrayList(new ItemStack(Items.GOLDEN_AXE)),
                Lists.newArrayList(Utils.getEnchantedBook(VTweaks.lumbering))));

        recipes.add(registration.getVanillaRecipeFactory().createAnvilRecipe(
                Utils.getEnchantedBook(Enchantments.UNBREAKING, 3),
                Lists.newArrayList(Utils.getEnchantedBook(Enchantments.UNBREAKING, 3)),
                Lists.newArrayList(Utils.getEnchantedBook(VTweaks.imperishable))));

        registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
    }
}