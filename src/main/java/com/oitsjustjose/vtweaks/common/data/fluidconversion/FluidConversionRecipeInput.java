package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record FluidConversionRecipeInput(ItemStack input) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int idx) {
        return idx == 0 ? this.input : ItemStack.EMPTY;
    }

    public int size() {
        return 1;
    }
}
