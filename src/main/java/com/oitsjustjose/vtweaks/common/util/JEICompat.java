package com.oitsjustjose.vtweaks.common.util;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;

@JeiPlugin
public class JEICompat implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "ench_books");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
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