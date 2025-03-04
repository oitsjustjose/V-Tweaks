package com.oitsjustjose.vtweaks.integration.jei;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import com.oitsjustjose.vtweaks.common.util.Constants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jei_recipes");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.DISPENSER), FluidConversionRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(Items.WATER_BUCKET), FluidConversionRecipeCategory.TYPE);
        registration.addRecipeCatalyst(FluidConversionRecipeCategory.SPLASH_POTION, FluidConversionRecipeCategory.TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();
        var guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new FluidConversionRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null) {
            VTweaks.getInstance().LOGGER.info("Client level is null! JEI will not work");
            return;
        }

        var recipeManager = level.getRecipeManager();
        registration.addRecipes(RecipeTypes.ANVIL, generateAnvilRecipes(registration, recipeManager));
        registration.addRecipes(FluidConversionRecipeCategory.TYPE, generateFluidConversionRecipes(registration, recipeManager));
//        registration.addRecipes(FluidConversionRecipeCategory.TYPE, VTweaks.getInstance().getFluidConversionRecipes().values().stream().toList());
    }

    private List<IJeiAnvilRecipe> generateAnvilRecipes(IRecipeRegistration registration, RecipeManager mgr) {
        List<IJeiAnvilRecipe> ret = Lists.newArrayList();

        for (var recipe : mgr.getAllRecipesFor(ModRecipeTypes.ANVIL.get())) {
            var left = Arrays.asList(recipe.value().getLeft().getItems());
            var right = Arrays.asList(recipe.value().getRight().getItems());
            var result = List.of(recipe.value().getResult());
            ret.add(registration.getVanillaRecipeFactory().createAnvilRecipe(left, right, result, recipe.id()));
        }

        return ret;
    }

    private List<FluidConversionRecipe> generateFluidConversionRecipes(IRecipeRegistration registration, RecipeManager mgr) {
        List<FluidConversionRecipe> ret = Lists.newArrayList();

        for (var recipe : mgr.getAllRecipesFor(ModRecipeTypes.FLUID_CONVERSION.get())) {
            ret.add(recipe.value());
        }

        return ret;
    }
}