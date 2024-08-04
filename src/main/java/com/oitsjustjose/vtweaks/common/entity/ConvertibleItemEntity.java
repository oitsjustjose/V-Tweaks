package com.oitsjustjose.vtweaks.common.entity;

import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ConvertibleItemEntity extends ItemEntity {
    private final List<FluidConversionRecipe> recipes;
    private boolean hasBeenConverted;

    public ConvertibleItemEntity(ItemEntity item, List<RecipeHolder<FluidConversionRecipe>> Recipes) {
        super(item.level(), item.getX(), item.getY(), item.getZ(), item.getItem());
        this.recipes = Recipes.stream().map(RecipeHolder::value).toList();
        this.hasBeenConverted = false;
        this.setDeltaMovement(item.getDeltaMovement());
        this.setPickUpDelay(40);
    }

    @Override
    public void tick() {
        if (!this.hasBeenConverted) {
            var fluidState = this.level().getFluidState(this.blockPosition());
            if (!fluidState.isEmpty()) {
                var rl = NeoForgeRegistries.FLUID_TYPES.getKey(fluidState.getFluidType());
                var recipe = this.recipes.stream().filter(x -> x.getFluid().equals(rl)).findFirst();
                if (recipe.isPresent()) {
                    var clone = recipe.get().getResult().copy();
                    clone.setCount(this.getItem().getCount());

                    if (!this.getItem().getComponents().isEmpty()) {
                        clone.applyComponents(this.getItem().getComponents());
                    }
                    this.setItem(clone);
                    this.hasBeenConverted = true;
                }
            }
        }
        super.tick();
    }
}
