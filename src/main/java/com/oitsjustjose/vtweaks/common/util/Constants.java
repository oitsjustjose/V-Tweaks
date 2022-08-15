package com.oitsjustjose.vtweaks.common.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class Constants {
    public static final String MODID = "vtweaks";
    public static final String MODNAME = "V-Tweaks";

    public static final ResourceLocation ENTITY_CAP = new ResourceLocation(MODID, "entity_nbt_storage");
    public static final TagKey<Item> IGNITION = ItemTags.create(new ResourceLocation(MODID, "ignition_item"));
    public static final TagKey<EntityType<?>> CREEPERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY,
            new ResourceLocation("forge", "creepers"));
}
