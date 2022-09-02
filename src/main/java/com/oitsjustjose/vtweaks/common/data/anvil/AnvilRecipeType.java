package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.world.item.crafting.RecipeType;

public class AnvilRecipeType implements RecipeType<AnvilRecipe> {
    @Override
    public String toString() {
        return Constants.MODID + ":anvil";
    }
}
