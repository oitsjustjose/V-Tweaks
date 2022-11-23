package com.oitsjustjose.vtweaks.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ConvertibleItemEntity extends ItemEntity {
    private final ItemStack output;
    private final ResourceLocation fluid;
    private boolean hasBeenConverted;

    public ConvertibleItemEntity(ItemEntity item, ItemStack output, ResourceLocation fluid) {
        super(item.getLevel(), item.getX(), item.getY(), item.getZ(), item.getItem());
        this.output = output;
        this.fluid = fluid;
        this.hasBeenConverted = false;
        this.setDeltaMovement(item.getDeltaMovement());
        this.setPickUpDelay(40);
    }

    @Override
    public void tick() {
        if (!this.hasBeenConverted) {
            var fluidState = this.level.getFluidState(this.blockPosition());
            if (!fluidState.isEmpty()) {
                ResourceLocation rl = ForgeRegistries.FLUID_TYPES.get().getKey(fluidState.getFluidType());
                if (rl != null && rl.equals(this.fluid)) {
                    ItemStack clone = this.output.copy();
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
