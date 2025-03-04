package com.oitsjustjose.vtweaks.common.data.anvil;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record AnvilRecipeInput(ItemStack left, ItemStack right) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int idx) {
        return switch (idx) {
            case 0 -> this.left;
            case 1 -> this.right;
            default -> ItemStack.EMPTY;
        };
    }

    public int size() {
        return 2;
    }
}
