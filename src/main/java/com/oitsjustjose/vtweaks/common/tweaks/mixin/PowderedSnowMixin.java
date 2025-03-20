package com.oitsjustjose.vtweaks.common.tweaks.mixin;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.tweaks.block.PowderSnowParticleHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public abstract class PowderedSnowMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void PowderSnowBlock(BlockBehaviour.Properties properties, CallbackInfo ci) {
        if (CommonConfig.EnablePowderedSnowMixin.get()) {
            if (!PowderSnowParticleHandler.hasRegistered()) {
                NeoForge.EVENT_BUS.register(new PowderSnowParticleHandler());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getCollisionShape", cancellable = true)
    protected void getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cb) {
        if (CommonConfig.EnablePowderedSnowMixin.get()) {
            cb.setReturnValue(Shapes.block());
        }
    }
}