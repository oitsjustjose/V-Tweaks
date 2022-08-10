package com.oitsjustjose.vtweaks.common.entity.culling.capability;

import net.minecraft.nbt.CompoundTag;

public interface INBTCapability {
    void putFlag(String key);

    boolean getFlag(String key);

    void removeFlag(String key);

    CompoundTag serialize();

    void deserialize(CompoundTag cmp);
}
