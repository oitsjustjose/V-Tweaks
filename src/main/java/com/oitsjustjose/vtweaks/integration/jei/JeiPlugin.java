package com.oitsjustjose.vtweaks.integration.jei;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.I18n;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
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
        var dispenser = new ItemStack(Blocks.DISPENSER);
        var comp = I18n.Translate("vtweaks.dispenser.jei.title").plainCopy().withStyle(ChatFormatting.RESET);
        dispenser.set(DataComponents.ITEM_NAME, comp);

        registration.addRecipeCatalyst(dispenser, FluidConversionRecipeCategory.TYPE);

        // Grab all fluid buckets for fluids that are used in any/all recipe sets

        var fluids = new HashSet<ResourceLocation>();

        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null) return;

        for (var recipe : level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FLUID_CONVERSION.get())) {
            fluids.add(recipe.value().getFluid());
        }

        for (var fluidKey : fluids) {
            var fluid = BuiltInRegistries.FLUID.get(fluidKey);
            var bucket = fluid.getBucket();
            if (bucket == Items.AIR) continue;
            registration.addRecipeCatalyst(new ItemStack(bucket), FluidConversionRecipeCategory.TYPE);
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();
        var guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new FluidConversionRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        var mc = Minecraft.getInstance();
        var level = mc.level;
        if (level == null) {
            VTweaks.getInstance().LOGGER.info("Client level is null! JEI plugin will not work");
            return;
        }

        var recipeManager = level.getRecipeManager();
        registration.addRecipes(RecipeTypes.ANVIL, generateAnvilRecipes(registration, recipeManager));
        registration.addRecipes(FluidConversionRecipeCategory.TYPE, generateFluidConversionRecipes(recipeManager));
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

    private List<FluidConversionRecipe> generateFluidConversionRecipes(RecipeManager mgr) {
        return mgr.getAllRecipesFor(ModRecipeTypes.FLUID_CONVERSION.get()).stream().map(RecipeHolder::value).toList();
    }
}