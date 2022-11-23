package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.entity.ConvertibleItemEntity;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FluidConversionDispensing extends DefaultDispenseItemBehavior {
    @Override
    public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        var facing = source.getBlockState().getValue(DispenserBlock.FACING);
        var pos = DispenserBlock.getDispensePosition(source);
        var itemStack = stack.split(1);
        this.dispense(source.getLevel(), itemStack, 6, facing, pos);
        return stack;
    }

    public void dispense(Level level, ItemStack stack, int speed, Direction facing, Position pos) {
        var d0 = pos.x();
        var d1 = pos.y();
        var d2 = pos.z();

        d1 = d1 - (facing.getAxis() == Direction.Axis.Y ? 0.125D : 0.15625D);

        var entityItem = new ItemEntity(level, d0, d1, d2, stack);
        var recipe = findRecipe(level, stack);
        if (recipe.isPresent()) {
            entityItem = new ConvertibleItemEntity(entityItem, recipe.get().getResult(), recipe.get().getFluid());
        }

        var d3 = level.getRandom().nextDouble() * 0.1D + 0.2D;
        var motX = (double) facing.getStepX() * d3;
        var motY = 0.20000000298023224D;
        var motZ = (double) facing.getStepZ() * d3;
        motX += level.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        motY += level.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        motZ += level.getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        entityItem.setDeltaMovement(motX, motY, motZ);

        // Dispensed items have no pickup delay so.....
        entityItem.setNoPickUpDelay();
        level.addFreshEntity(entityItem);
    }

    public Optional<FluidConversionRecipe> findRecipe(Level level, ItemStack stack) {
        var handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, stack);
        return level.getRecipeManager().getRecipeFor(VTweaks.CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(handler), level);
    }
}
