package com.oitsjustjose.vtweaks.common.tweaks.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class DestroySpeedMixin {
    @Inject(at = @At("HEAD"), method = "getDestroySpeed", cancellable = true)
    public void getDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
        var tool = stack.get(DataComponents.TOOL);
        if (tool != null) {
            var speed = tool.getMiningSpeed(state);

            if (stack.isDamageableItem()) {
                var maxDamage = stack.getMaxDamage();
                var currentDamage = stack.getDamageValue();
                var scale = 1.F - ((float) currentDamage / (float) maxDamage);
                cir.setReturnValue(speed * Math.max(scale, 0.2F));
            }
        }
    }
}
