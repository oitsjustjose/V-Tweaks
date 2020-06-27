package com.oitsjustjose.vtweaks.common.world.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class VTweaksCapabilityStorage implements Capability.IStorage<IVTweaksCapability> {
    @Override
    public void readNBT(Capability<IVTweaksCapability> capability, IVTweaksCapability instance, Direction side,
            INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            instance.deserializeNBT(((CompoundNBT) nbt));
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IVTweaksCapability> capability, IVTweaksCapability instance, Direction side) {
        // Initialize the Compound with WorldDeposits and RetroGen:
        return instance.serializeNBT();
    }
}
