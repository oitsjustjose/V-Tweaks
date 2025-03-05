package com.oitsjustjose.vtweaks.common.registries;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);

    public static final Supplier<RecipeType<AnvilRecipe>> ANVIL = RECIPE_TYPES.register("anvil", AnvilRecipe.Type::new);
    public static final Supplier<RecipeType<FluidConversionRecipe>> FLUID_CONVERSION = RECIPE_TYPES.register("fluid_conversion", FluidConversionRecipe.Type::new);
}
