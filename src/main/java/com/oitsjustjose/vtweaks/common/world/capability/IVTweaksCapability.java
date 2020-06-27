package com.oitsjustjose.vtweaks.common.world.capability;

import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IVTweaksCapability extends INBTSerializable<CompoundNBT> {
    boolean hasPlayerSeenWelcome(UUID uuid);

    void setPlayerSeenWelcome(UUID uuid);

    Map<UUID, Boolean> getPlayerSeenWelcome();
}