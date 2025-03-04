package com.oitsjustjose.vtweaks.common.registries;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);

    public static final Supplier<RecipeSerializer<AnvilRecipe>> ANVIL = RECIPE_SERIALIZERS.register("anvil", AnvilRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<FluidConversionRecipe>> FLUID_CONVERSION = RECIPE_SERIALIZERS.register("fluid_conversion", FluidConversionRecipe.Serializer::new);
}
