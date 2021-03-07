package com.oitsjustjose.vtweaks.common.world.capability;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.CompoundNBT;

public class VTweaksCapability implements IVTweaksCapability {
    private final Map<UUID, Boolean> msgMap;

    public VTweaksCapability() {
        this.msgMap = new ConcurrentHashMap<>();
    }

    @Override
    public void setPlayerSeenWelcome(UUID uuid) {
        this.msgMap.put(uuid, true);
    }

    @Override
    public boolean hasPlayerSeenWelcome(UUID uuid) {
        return this.msgMap.containsKey(uuid);
    }

    @Override
    public Map<UUID, Boolean> getPlayerSeenWelcome() {
        return this.msgMap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("PlayersShownWelcome", new CompoundNBT());

        CompoundNBT playersShown = compound.getCompound("PlayersShownWelcome");

        this.msgMap.forEach((x, y) -> playersShown.putBoolean(x.toString(), y));

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        CompoundNBT playersShown = compound.getCompound("PlayersShownWelcome");

        playersShown.keySet().forEach(x -> this.setPlayerSeenWelcome(UUID.fromString(x)));
    }
}