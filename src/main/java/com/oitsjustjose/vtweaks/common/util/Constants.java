package com.oitsjustjose.vtweaks.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Constants {
    public static final String MODID = "vtweaks";
    public static final String MODNAME = "V-Tweaks";

    public static final TagKey<Item> IGNITION = ItemTags.create(new ResourceLocation(MODID, "ignition_item"));
}
