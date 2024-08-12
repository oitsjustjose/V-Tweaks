package com.oitsjustjose.vtweaks.common.registries;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipeSerializer;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipeSerializer;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class VtweaksRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<Recipe<RecipeWrapper>>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final Supplier<RecipeType<FluidConversionRecipe>> FLUID_CONVERSION_RECIPE_TYPE = RECIPE_TYPES.register("fluid_conversion", () -> RecipeType.<FluidConversionRecipe>simple(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fluid_conversion")));
    public static final Supplier<RecipeType<AnvilRecipe>> ANVIL_RECIPE_TYPE = RECIPE_TYPES.register("anvil", () -> RecipeType.<AnvilRecipe>simple(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "anvil")));

    public static final Supplier<RecipeSerializer<FluidConversionRecipe>> FLUID_CONVERSION_SERIALIZER = RECIPE_SERIALIZERS.register("fluid_conversion", FluidConversionRecipeSerializer::new);
    public static final Supplier<RecipeSerializer<AnvilRecipe>> ANVIL_SERIALIZER = RECIPE_SERIALIZERS.register("anvil", AnvilRecipeSerializer::new);
}
