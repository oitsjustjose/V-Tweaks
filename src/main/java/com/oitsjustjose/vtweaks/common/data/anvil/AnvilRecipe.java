package com.oitsjustjose.vtweaks.common.data.anvil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeSerializers;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AnvilRecipe implements Recipe<AnvilRecipeInput> {
    private final Ingredient left;
    private final Ingredient right;
    private final ItemStack result;
    private final int cost;
    private final boolean copyNbtFromLeft;
    private final boolean copyNbtFromRight;
    private final boolean strictMatch;

    public AnvilRecipe(Ingredient leftIn, Ingredient rightIn, ItemStack resultIn, int xpCostIn, boolean copyComponentsFromLeftIn, boolean copyComponentsFromRightIn, boolean strictMatchIn) {
        this.left = leftIn;
        this.right = rightIn;
        this.result = resultIn;
        this.cost = xpCostIn;
        this.copyNbtFromLeft = copyComponentsFromLeftIn;
        this.copyNbtFromRight = copyComponentsFromRightIn;
        this.strictMatch = strictMatchIn;
    }

    public Ingredient getLeft() {
        return this.left;
    }

    public Ingredient getRight() {
        return this.right;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public int getCost() {
        return this.cost;
    }

    public boolean copyComponentsFromLeft() {
        return this.copyNbtFromLeft;
    }

    public boolean copyComponentsFromRight() {
        return this.copyNbtFromRight;
    }

    public boolean isStrictMatch() {
        return this.strictMatch;
    }

    @Override
    public boolean matches(@NotNull AnvilRecipeInput anvilRecipeInput, @NotNull Level level) {
        var l = anvilRecipeInput.getItem(0);
        var r = anvilRecipeInput.getItem(1);

        if (!(this.left.test(l) && this.right.test(r))) return false;

        /* Recipe inputs match so that's enough for us :) */
        if (!isStrictMatch()) return true;

        var leftIngredientMatch = Arrays.stream(this.left.getItems()).filter(x -> x.getItem() == l.getItem()).findFirst().orElse(ItemStack.EMPTY);
        var rightIngredientMatch = Arrays.stream(this.right.getItems()).filter(x -> x.getItem() == r.getItem()).findFirst().orElse(ItemStack.EMPTY);

        if (rightIngredientMatch.isEmpty() || leftIngredientMatch.isEmpty()) {
            VTweaks.getInstance().LOGGER.error("Anvil recipe [{} + {}] met an invalid state in AnvilRecipes#matches", l.toString(), r.toString());
            return false;
        }

        boolean leftCompVerified = leftIngredientMatch.getComponents().isEmpty() && l.getComponents().isEmpty();
        if (!leftIngredientMatch.getComponents().isEmpty()) {
            leftCompVerified = !l.getComponents().isEmpty() && doComponentsMatch(leftIngredientMatch.getComponents(), l.getComponents());
        }

        boolean rightCompVerified = rightIngredientMatch.getComponents().isEmpty() && r.getComponents().isEmpty();
        if (!rightIngredientMatch.getComponents().isEmpty()) {
            rightCompVerified = !r.getComponents().isEmpty() && doComponentsMatch(rightIngredientMatch.getComponents(), r.getComponents());
        }

        return leftCompVerified && rightCompVerified;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull AnvilRecipeInput __, HolderLookup.@NotNull Provider ___) {
        return this.result.copy();
    }

    private boolean doComponentsMatch(DataComponentMap orig, DataComponentMap comp) {
        // If these aren't the same size, there's no way they'll match.
        if (orig.size() == comp.size()) return false;

        for (var origKey : orig.keySet()) {
            var value = orig.get(origKey);

            if (comp.has(origKey)) {
                if (comp.get(origKey) != value) {
                    return false;
                }
            } else { /* The component to compare is missing a required key -- these don't match */
                return false;
            }
        }

        // We know that all required components exist and that they're the same sizes, so this is a 1:1 match!
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider __) {
        return this.result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.ANVIL.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.ANVIL.get();
    }

    public static class Serializer implements RecipeSerializer<AnvilRecipe> {
        public static final MapCodec<AnvilRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("left").forGetter(AnvilRecipe::getLeft),
                Ingredient.CODEC.fieldOf("right").forGetter(AnvilRecipe::getRight),
                ItemStack.CODEC.fieldOf("result").forGetter(AnvilRecipe::getResult),
                Codec.INT.fieldOf("xpCost").forGetter(AnvilRecipe::getCost),
                Codec.BOOL.fieldOf("copyCompsFromLeft").forGetter(AnvilRecipe::copyComponentsFromLeft),
                Codec.BOOL.fieldOf("copyCompsFromRight").forGetter(AnvilRecipe::copyComponentsFromRight),
                Codec.BOOL.fieldOf("strict").forGetter(AnvilRecipe::isStrictMatch)
        ).apply(inst, AnvilRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> STREAM_CODEC = StreamCodec.of(
                AnvilRecipe.Serializer::toNetwork,
                AnvilRecipe.Serializer::fromNetwork
        );

        public static void toNetwork(RegistryFriendlyByteBuf buf, AnvilRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getLeft());
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getRight());
            ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
            buf.writeInt(recipe.getCost());
            buf.writeBoolean(recipe.copyComponentsFromLeft());
            buf.writeBoolean(recipe.copyComponentsFromRight());
            buf.writeBoolean(recipe.isStrictMatch());
        }

        public static AnvilRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            var left = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            var right = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
            var output = ItemStack.STREAM_CODEC.decode(buf);
            var cost = buf.readInt();
            var cpFromLeft = buf.readBoolean();
            var cpFromRight = buf.readBoolean();
            var strict = buf.readBoolean();
            return new AnvilRecipe(left, right, output, cost, cpFromLeft, cpFromRight, strict);
        }

        @Override
        public @NotNull MapCodec<AnvilRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, AnvilRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<AnvilRecipe> {
        @Override
        public String toString() {
            return Constants.MOD_ID + ":anvil";
        }
    }
}
