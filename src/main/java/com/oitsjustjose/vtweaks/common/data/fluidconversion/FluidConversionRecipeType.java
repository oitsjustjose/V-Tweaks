package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.world.item.crafting.RecipeType;

public class FluidConversionRecipeType implements RecipeType<FluidConversionRecipe> {
    @Override
    public String toString() {
        return Constants.MOD_ID + ":fluid_conversion";
    }
}
