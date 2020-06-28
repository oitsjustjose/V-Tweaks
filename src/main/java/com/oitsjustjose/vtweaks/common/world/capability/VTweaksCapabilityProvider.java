package com.oitsjustjose.vtweaks.common.world.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class VTweaksCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IVTweaksCapability impl = new VTweaksCapability();
    private final LazyOptional<IVTweaksCapability> cap = LazyOptional.of(() -> impl);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capIn, final @Nullable Direction side) {
        if (capIn == VTweaks.VTWEAKS_CAPABILITY) {
            return cap.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return impl.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        impl.deserializeNBT(nbt);
    }

}