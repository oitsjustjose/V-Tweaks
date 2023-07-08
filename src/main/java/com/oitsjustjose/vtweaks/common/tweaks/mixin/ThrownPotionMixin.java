package com.oitsjustjose.vtweaks.common.tweaks.mixin;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin {
    @Inject(at = @At("HEAD"), method = "dowseFire(Lnet/minecraft/core/BlockPos;)V", cancellable = true)
    protected void onHitBlock(BlockPos pos, CallbackInfo ci) {
        if (CommonConfig.EnableSplashPotionMixin.get()) {
            var base = (Entity) (Object) this;
            var state = base.level().getBlockState(pos);
            var item = state.getBlock().asItem();
            var foundRecipe = findRecipe(base.level(), new ItemStack(item));
            if (foundRecipe.isEmpty()) return;
            var recipe = foundRecipe.get();
            if (recipe.getFluid().getPath().equalsIgnoreCase("water")) {
                if (recipe.getResult().getItem() instanceof BlockItem blockItem) {
                    base.level().setBlock(pos, blockItem.getBlock().withPropertiesOf(state), 3);
                }
            }
        }
    }

    public Optional<FluidConversionRecipe> findRecipe(Level level, ItemStack item) {
        var handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, item);
        return level.getRecipeManager().getRecipeFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(handler), level);
    }
}
