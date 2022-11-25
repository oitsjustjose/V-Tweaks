package com.oitsjustjose.vtweaks.integration.jei;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "jei_recipes");
    public static HashMap<ResourceLocation, AnvilRecipe> AllAnvilRecipes = new HashMap<>();
    public static HashMap<ResourceLocation, FluidConversionRecipe> AllFluidConversionRecipes = new HashMap<>();

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();
        var guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new FluidConversionRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var anvilRecipes = AllAnvilRecipes.values().stream().map(x -> registration.getVanillaRecipeFactory().createAnvilRecipe(x.getLeft(), List.of(x.getRight()), List.of(x.getResult()))).toList();
        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
        registration.addRecipes(FluidConversionRecipeCategory.TYPE, AllFluidConversionRecipes.values().stream().toList());
    }
}