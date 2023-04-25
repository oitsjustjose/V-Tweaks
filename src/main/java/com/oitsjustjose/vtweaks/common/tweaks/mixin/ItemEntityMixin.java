package com.oitsjustjose.vtweaks.common.tweaks.mixin;

import com.oitsjustjose.vtweaks.common.config.MixinConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(at = @At("HEAD"), method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", cancellable = true)
    private void hurt(DamageSource source, float amt, CallbackInfoReturnable<Boolean> callback) {
        if (MixinConfig.EnableCactusMixin.get() && source.is(DamageTypes.CACTUS)) {
            callback.setReturnValue(false);
        }
    }
}
