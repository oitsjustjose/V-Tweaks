package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.common.data.helpers.VTJsonHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidConversionRecipeSerializer implements RecipeSerializer<FluidConversionRecipe> {
    @Override
    public @NotNull FluidConversionRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject obj) {
        var input = Ingredient.fromJson(obj.get("input"), false);
        var output = VTJsonHelpers.deserializeItemStack(obj, "output");
        var fluid = new ResourceLocation(obj.get("fluid").getAsString());
        return new FluidConversionRecipe(id, input, output, fluid);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull FluidConversionRecipe recipe) {
        CraftingHelper.write(buf, recipe.getInput());
        buf.writeItemStack(recipe.getResult(), false);
        buf.writeUtf(recipe.getFluid().toString());
    }

    @Override
    public @Nullable FluidConversionRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var input = Ingredient.fromNetwork(buf);
        var output = buf.readItem();
        var fluid = new ResourceLocation(buf.readUtf());
        return new FluidConversionRecipe(id, input, output, fluid);
    }
}
