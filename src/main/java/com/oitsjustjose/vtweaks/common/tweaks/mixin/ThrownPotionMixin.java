package com.oitsjustjose.vtweaks.common.tweaks.mixin;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipeInput;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin {
    @Inject(at = @At("HEAD"), method = "dowseFire(Lnet/minecraft/core/BlockPos;)V")
    protected void onHitBlock(BlockPos pos, CallbackInfo ci) {
        if (CommonConfig.EnableSplashPotionMixin.get()) {
            var base = (Entity) (Object) this;
            var state = base.level().getBlockState(pos);
            var item = state.getBlock().asItem();
            var foundRecipe = v_Tweaks$findRecipe(base.level(), new ItemStack(item));
            if (foundRecipe.isEmpty()) return;
            var recipe = foundRecipe.get().value();
            if (recipe.getFluid().getPath().equalsIgnoreCase("water")) {
                if (recipe.getResult().getItem() instanceof BlockItem blockItem) {
                    base.level().setBlock(pos, blockItem.getBlock().withPropertiesOf(state), 3);
                }
            }
        }
    }

    @Unique
    public Optional<RecipeHolder<FluidConversionRecipe>> v_Tweaks$findRecipe(Level level, ItemStack item) {
        var input = new FluidConversionRecipeInput(item);
        return level.getRecipeManager().getRecipeFor(ModRecipeTypes.FLUID_CONVERSION.get(), input, level);
    }
}
