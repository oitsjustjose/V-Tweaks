package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeSerializers;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import com.oitsjustjose.vtweaks.common.tweaks.recipe.FluidConversionDispensing;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class FluidConversionRecipe implements Recipe<FluidConversionRecipeInput> {
    private final Ingredient input;
    private final ItemStack result;
    private final ResourceLocation fluid;

    public FluidConversionRecipe(Ingredient input, ItemStack output, ResourceLocation fluid) {
        this.input = input;
        this.result = output;
        this.fluid = fluid;
        for (var stack : this.input.getItems()) {
            DispenserBlock.registerBehavior(stack.getItem(), new FluidConversionDispensing());
        }
    }

    @Override
    public boolean matches(FluidConversionRecipeInput recipeInput, @NotNull Level __) {
        return this.input.test(recipeInput.input());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull FluidConversionRecipeInput __, @NotNull HolderLookup.Provider ___) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider __) {
        return this.result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FLUID_CONVERSION.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.FLUID_CONVERSION.get();
    }

    public Ingredient getInput() { return this.input; }

    public ItemStack getResult() { return this.result; }

    public ResourceLocation getFluid() { return this.fluid; }

    public static class Serializer implements RecipeSerializer<FluidConversionRecipe> {
        public static final MapCodec<FluidConversionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                Ingredient.CODEC.fieldOf("input").forGetter(FluidConversionRecipe::getInput),
                ItemStack.CODEC.fieldOf("output").forGetter(FluidConversionRecipe::getResult),
                ResourceLocation.CODEC.fieldOf("fluid").forGetter(FluidConversionRecipe::getFluid)
            ).apply(inst, FluidConversionRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, FluidConversionRecipe> STREAM_CODEC = StreamCodec.of(
                FluidConversionRecipe.Serializer::toNetwork,
                FluidConversionRecipe.Serializer::fromNetwork
        );

        public static void toNetwork(RegistryFriendlyByteBuf buf, FluidConversionRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getInput());
            ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
            ResourceLocation.STREAM_CODEC.encode(buf, recipe.getFluid());
        }

        public static FluidConversionRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            var result = ItemStack.STREAM_CODEC.decode(buf);
            var fluid = ResourceLocation.STREAM_CODEC.decode(buf);
            return new FluidConversionRecipe(input, result, fluid);
        }

        @Override
        public @NotNull MapCodec<FluidConversionRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, FluidConversionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<FluidConversionRecipe> {
        @Override
        public String toString() {
            return Constants.MOD_ID + ":fluid_conversion";
        }
    }
}
