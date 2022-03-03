package com.oitsjustjose.vtweaks.common.entity;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.JsonUtils;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ChallengerMobSerialization {
    @Nonnull
    public static ItemStack deserializeItemStack(JsonObject obj, String key) {
        return ItemStack.read(Objects.requireNonNull(JsonUtils.readNBT(obj, key)));
    }
}
