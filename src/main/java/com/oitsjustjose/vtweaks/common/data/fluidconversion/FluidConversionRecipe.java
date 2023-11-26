package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.tweaks.recipe.FluidConversionDispensing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.items.wrapper.RecipeWrapper;
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

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper p_44001_) {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        return this.input.test(wrapper.getItem(0));
    }
}
