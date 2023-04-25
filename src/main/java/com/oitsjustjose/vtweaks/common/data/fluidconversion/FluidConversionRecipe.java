package com.oitsjustjose.vtweaks.common.data.fluidconversion;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.tweaks.recipe.FluidConversionDispensing;
import com.oitsjustjose.vtweaks.integration.jei.JeiPlugin;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class FluidConversionRecipe implements Recipe<RecipeWrapper> {
    public final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack result;
    private final ResourceLocation fluid;

    public FluidConversionRecipe(ResourceLocation id, ItemStack input, ItemStack output, ResourceLocation fluid) {
        this.id = id;
        this.input = input;
        this.result = output;
        this.fluid = fluid;
        JeiPlugin.AllFluidConversionRecipes.put(id, this);
        DispenserBlock.registerBehavior(this.input.getItem(), new FluidConversionDispensing());
    }

    public ItemStack getInput() {
        return this.input.copy();
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public ResourceLocation getFluid() {
        return this.fluid;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        var wrapperStack = wrapper.getItem(0);
        return wrapperStack.getItem() == this.input.getItem() && ItemStack.tagMatches(wrapperStack, this.input);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper p_44001_, @NotNull RegistryAccess p_267165_) {
        return this.result;
    }


    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess p_267052_) {
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
}
