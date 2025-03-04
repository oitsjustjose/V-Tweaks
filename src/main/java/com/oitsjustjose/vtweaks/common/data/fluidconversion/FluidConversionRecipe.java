package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.registries.VTweaksRegistry;
import com.oitsjustjose.vtweaks.common.tweaks.recipe.FluidConversionDispensing;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class FluidConversionRecipe implements Recipe<RecipeWrapper> {
    public final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack result;
    private final ResourceLocation fluid;

    public FluidConversionRecipe(ResourceLocation id, Ingredient input, ItemStack output, ResourceLocation fluid) {
        this.id = id;
        this.input = input;
        this.result = output;
        this.fluid = fluid;
        for (var stack : this.input.getItems()) {
            DispenserBlock.registerBehavior(stack.getItem(), new FluidConversionDispensing());
        }
        VTweaks.getInstance().addFluidConversionRecipe(id, this);
    }

    public Ingredient getInput() {
        return this.input;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public ResourceLocation getFluid() {
        return this.fluid;
    }

    public ResourceLocation getId() { return this.id; }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper p_44001_, @NotNull HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider pRegistries) {
        return this.result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTweaksRegistry.FLUID_CONVERSION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return VTweaksRegistry.FLUID_CONVERSION_RECIPE_TYPE.get();
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        return this.input.test(wrapper.getItem(0));
    }
}
