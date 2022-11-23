package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.common.data.helpers.VTJsonHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidConversionRecipeSerializer implements RecipeSerializer<FluidConversionRecipe> {
    @Override
    public @NotNull FluidConversionRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject obj) {
        ItemStack input = VTJsonHelpers.deserialize(obj, "input");
        ItemStack output = VTJsonHelpers.deserialize(obj, "output");
        ResourceLocation fluid = new ResourceLocation(obj.get("fluid").getAsString());
        return new FluidConversionRecipe(id, input, output, fluid);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull FluidConversionRecipe recipe) {
        buf.writeItemStack(recipe.getInput(), false);
        buf.writeItemStack(recipe.getResult(), false);
        buf.writeUtf(recipe.getFluid().toString());
    }

    @Override
    public @Nullable FluidConversionRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var input = buf.readItem();
        var output = buf.readItem();
        var fluid = new ResourceLocation(buf.readUtf());
        return new FluidConversionRecipe(id, input, output, fluid);
    }
}
