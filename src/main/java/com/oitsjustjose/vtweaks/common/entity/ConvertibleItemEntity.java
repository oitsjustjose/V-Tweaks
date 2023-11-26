package com.oitsjustjose.vtweaks.common.entity;

import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ConvertibleItemEntity extends ItemEntity {
    private final List<FluidConversionRecipe> recipes;
    private boolean hasBeenConverted;

    public ConvertibleItemEntity(ItemEntity item, List<FluidConversionRecipe> Recipes) {
        super(item.getLevel(), item.getX(), item.getY(), item.getZ(), item.getItem());
        this.recipes = Recipes;
        this.hasBeenConverted = false;
        this.setDeltaMovement(item.getDeltaMovement());
        this.setPickUpDelay(40);
    }

    @Override
    public void tick() {
        if (!this.hasBeenConverted) {
            var fluidState = this.getLevel().getFluidState(this.blockPosition());
            if (!fluidState.isEmpty()) {
                var rl = ForgeRegistries.FLUID_TYPES.get().getKey(fluidState.getFluidType());
                var recipe = this.recipes.stream().filter(x -> x.getFluid().equals(rl)).findFirst();
                if (recipe.isPresent()) {
                    var clone = recipe.get().getResult().copy();
                    clone.setCount(this.getItem().getCount());
                    if (this.getItem().hasTag()) {
                        clone.setTag(this.getItem().getTag());
                    }
                    this.setItem(clone);
                    this.hasBeenConverted = true;
                }
            }
        }
        super.tick();
    }
}
