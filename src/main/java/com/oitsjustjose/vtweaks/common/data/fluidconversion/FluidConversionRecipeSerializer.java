package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FluidConversionRecipeSerializer implements RecipeSerializer<FluidConversionRecipe> {
    public static final MapCodec<FluidConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(FluidConversionRecipe::getId),
            Ingredient.CODEC.fieldOf("input").forGetter(FluidConversionRecipe::getInput),
            ItemStack.CODEC.fieldOf("output").forGetter(FluidConversionRecipe::getResult),
            ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidConversionRecipe::getFluid)
    ).apply(inst, FluidConversionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidConversionRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC, FluidConversionRecipe::getId,
                    Ingredient.CONTENTS_STREAM_CODEC, FluidConversionRecipe::getInput,
                    ItemStack.STREAM_CODEC, FluidConversionRecipe::getResult,
                    ResourceLocation.STREAM_CODEC, FluidConversionRecipe::getFluid,
                    FluidConversionRecipe::new
            );

    @Override
    public MapCodec<FluidConversionRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FluidConversionRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
