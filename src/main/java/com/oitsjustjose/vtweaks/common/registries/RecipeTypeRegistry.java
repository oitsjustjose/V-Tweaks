package com.oitsjustjose.vtweaks.common.registries;

import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipeSerializer;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipeType;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeTypeRegistry {
    public final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);
    public final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Constants.MOD_ID);
//    public final RecipeSerializer<AnvilRecipe> ANVIL;
//    public final RecipeTypeRegistry<AnvilRecipe> ANVIL_RECIPE_TYPE;

    public final RecipeSerializer<FluidConversionRecipe> FLUID_CONVERSION;
    public final RecipeType<FluidConversionRecipe> FLUID_CONVERSION_RECIPE_TYPE;

    public RecipeTypeRegistry() {
        // Fluid Conversion
        this.FLUID_CONVERSION = new FluidConversionRecipeSerializer();
        this.FLUID_CONVERSION_RECIPE_TYPE = new FluidConversionRecipeType();
        SERIALIZERS.register("fluid_conversion", () -> this.FLUID_CONVERSION);
        TYPES.register("fluid_conversion", () -> this.FLUID_CONVERSION_RECIPE_TYPE);
    }
}
