package com.oitsjustjose.vtweaks.common.util;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@JeiPlugin
public class JEICompat implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "anvil_recipes");
    public static HashMap<ResourceLocation, AnvilRecipe> cache = new HashMap<>();

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<IJeiAnvilRecipe> r = cache.values().stream().map(x -> registration.getVanillaRecipeFactory().createAnvilRecipe(x.getLeft(), List.of(x.getRight()), List.of(x.getResult()))).toList();
        registration.addRecipes(r, VanillaRecipeCategoryUid.ANVIL);
    }
}