package com.oitsjustjose.vtweaks.common.data.anvil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oitsjustjose.vtweaks.common.network.codecs.StreamCodec8;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AnvilRecipeSerializer implements RecipeSerializer<AnvilRecipe> {
    public static final MapCodec<AnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(AnvilRecipe::getId),
            Ingredient.CODEC.fieldOf("left").forGetter(AnvilRecipe::getLeft),
            Ingredient.CODEC.fieldOf("right").forGetter(AnvilRecipe::getRight),
            ItemStack.CODEC.fieldOf("result").forGetter(AnvilRecipe::getResult),
            Codec.INT.fieldOf("xpCost").forGetter(AnvilRecipe::getCost),
            Codec.BOOL.fieldOf("copyCompsFromLeft").forGetter(AnvilRecipe::copyComponentsFromLeft),
            Codec.BOOL.fieldOf("copyCompsFromRight").forGetter(AnvilRecipe::copyComponentsFromRight),
            Codec.BOOL.fieldOf("strict").forGetter(AnvilRecipe::isStrictMatch)
    ).apply(inst, AnvilRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> STREAM_CODEC =
            StreamCodec8.composite(
                    ResourceLocation.STREAM_CODEC, AnvilRecipe::getId,
                    Ingredient.CONTENTS_STREAM_CODEC, AnvilRecipe::getLeft,
                    Ingredient.CONTENTS_STREAM_CODEC, AnvilRecipe::getRight,
                    ItemStack.STREAM_CODEC, AnvilRecipe::getResult,
                    ByteBufCodecs.INT, AnvilRecipe::getCost,
                    ByteBufCodecs.BOOL, AnvilRecipe::copyComponentsFromLeft,
                    ByteBufCodecs.BOOL, AnvilRecipe::copyComponentsFromRight,
                    ByteBufCodecs.BOOL, AnvilRecipe::isStrictMatch,
                    AnvilRecipe::new
            );


    @Override
    public MapCodec<AnvilRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
