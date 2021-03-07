package com.oitsjustjose.vtweaks.common.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockTweakConfig {
    private static final String CATEGORY_BLOCK_TWEAKS = "block tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_CROP_TWEAK;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CROP_TWEAK_BLACKLIST;
    public static ForgeConfigSpec.BooleanValue ENABLE_BONEMEAL_TWEAK;
    public static ForgeConfigSpec.BooleanValue ENABLE_CAKE_DROP;
    public static ForgeConfigSpec.BooleanValue ENABLE_TOOL_EFF_TWEAKS;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Block Tweaks").push(CATEGORY_BLOCK_TWEAKS);

        ENABLE_CROP_TWEAK = COMMON_BUILDER.comment("Allows for r-clicking to harvest most any (including mod) crops")
                .define("cropTweaksEnabled", true);
        CROP_TWEAK_BLACKLIST = COMMON_BUILDER.comment("A list of blocks to ignore, of form <modid:block>")
                .defineList("cropTweaksBlacklist", Lists.newArrayList(), (itemRaw) -> {
                    if (itemRaw instanceof String) {
                        String itemName = (String) itemRaw;
                        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(itemName)) != null;
                    }
                    return false;
                });
        ENABLE_BONEMEAL_TWEAK = COMMON_BUILDER.comment("Allows more plants to be bonemealed")
                .define("enableBonemealTweak", true);
        ENABLE_CAKE_DROP = COMMON_BUILDER.comment("Allows for uneaten cake to drop on break").define("enableCakeDrop",
                true);
        ENABLE_TOOL_EFF_TWEAKS = COMMON_BUILDER.comment("Fixes some tools NOT being effective on certain materials")
                .define("enableToolEffTweaks", true);

        COMMON_BUILDER.pop();
    }
}