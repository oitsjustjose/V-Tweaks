package com.oitsjustjose.vtweaks.common.entity.culling.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class NBTCapability implements INBTCapability {
    public static final Capability<INBTCapability> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    private CompoundTag tag;

    public NBTCapability() {
        this.tag = new CompoundTag();
    }

    public void putFlag(String key) {
        this.tag.putBoolean(key, true);
    }

    public boolean getFlag(String key) {
        return this.tag.contains(key) && this.tag.getBoolean(key);
    }

    public void removeFlag(String key) {
        this.tag.remove(key);
    }

    public CompoundTag serialize() {
        return this.tag;
    }

    public void deserialize(CompoundTag tag) {
        if (this.tag != null) {
            this.tag.merge(tag);
        } else {
            this.tag = tag;
        }
    }
}
