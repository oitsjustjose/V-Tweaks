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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class FluidConversionDispensing extends DefaultDispenseItemBehavior {
    @Override
    public @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack stack) {
        var facing = source.getBlockState().getValue(DispenserBlock.FACING);
        var pos = DispenserBlock.getDispensePosition(source);
        var itemStack = stack.split(1);
        this.dispense(source, itemStack, 6, facing, pos);
        return stack;
    }

    public void dispense(@NotNull BlockSource source, ItemStack stack, int speed, Direction facing, Position pos) {
        var recipes = findRecipes(source.getLevel(), stack);
        if (recipes.isEmpty()) { // Nothing to do -- do the default behavior
            super.dispense(source, stack);
            return;
        }

        var d0 = pos.x();
        var d1 = pos.y();
        var d2 = pos.z();
        d1 = d1 - (facing.getAxis() == Direction.Axis.Y ? 0.125D : 0.15625D);

        var entityItem = new ConvertibleItemEntity(new ItemEntity(source.getLevel(), d0, d1, d2, stack), recipes);

        var d3 = source.getLevel().getRandom().nextDouble() * 0.1D + 0.2D;
        var motX = (double) facing.getStepX() * d3;
        var motY = 0.20000000298023224D;
        var motZ = (double) facing.getStepZ() * d3;
        motX += source.getLevel().getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        motY += source.getLevel().getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        motZ += source.getLevel().getRandom().nextGaussian() * 0.007499999832361937D * (double) speed;
        entityItem.setDeltaMovement(motX, motY, motZ);

        // Dispensed items have no pickup delay so.....
        entityItem.setNoPickUpDelay();
        source.getLevel().addFreshEntity(entityItem);
    }

    public List<FluidConversionRecipe> findRecipes(Level level, ItemStack stack) {
        var recipes = level.getRecipeManager().getAllRecipesFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE);
        // Recipes use a stack size of 1, so set it to 1 to actually get a good compare
        var searchStack = stack.copy();
        searchStack.setCount(1);
        return recipes.stream().filter(x -> x.getInput().equals(searchStack, true)).collect(Collectors.toList());
    }
}
