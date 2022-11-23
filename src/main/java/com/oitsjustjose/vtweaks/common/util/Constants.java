package com.oitsjustjose.vtweaks.common.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class Constants {
    public static final String MOD_ID = "vtweaks";
    public static final TagKey<EntityType<?>> CREEPERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", "creepers"));
}
