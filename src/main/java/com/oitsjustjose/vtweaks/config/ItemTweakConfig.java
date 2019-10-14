package com.oitsjustjose.vtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ItemTweakConfig
{
    private static String CATEGORY_ITEM_TWEAKS = "item tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_EGG_HATCHING;
    public static ForgeConfigSpec.IntValue EGG_HATCING_CHANCE;
    public static ForgeConfigSpec.BooleanValue ENABLE_SAPLING_SELF_PLANTING;
    public static ForgeConfigSpec.IntValue DESPAWN_TIME_OVERRIDE;
    public static ForgeConfigSpec.BooleanValue ENABLE_CONCRETE_TWEAKS;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER)
    {
        COMMON_BUILDER.comment("Item Tweaks").push(CATEGORY_ITEM_TWEAKS);

        ENABLE_EGG_HATCHING = COMMON_BUILDER.comment("Allows egg items in the world to hatch instead of despawn")
                .define("eggHatchingEnabled", true);
        EGG_HATCING_CHANCE = COMMON_BUILDER
                .comment("The chance (out of 100 - higher means more frequent) that the egg will turn into a chick"
                        + "\nDO NOT SET THIS TOO HIGH OR ELSE CHICKENS MAY INFINITELY LAG YOUR WORLD")
                .defineInRange("eggHatchingChance", 512, 1, Integer.MAX_VALUE);
        ENABLE_SAPLING_SELF_PLANTING = COMMON_BUILDER
                .comment("Instead of de-spawning, saplings will attempt to plant themselves")
                .define("enableSaplingPlanting", true);
        DESPAWN_TIME_OVERRIDE = COMMON_BUILDER.comment(
                "Adjust Item Despawn Time (in ticks: 20 ticks in a second)\n-1 prevents items from despawning at all")
                .defineInRange("despawnTimeAdjustments", -1, -1, Integer.MAX_VALUE);
        ENABLE_CONCRETE_TWEAKS = COMMON_BUILDER
                .comment("Convert Concrete Powder to Concrete when the item is thrown into water")
                .define("enableConreteTweaks", true);

        COMMON_BUILDER.pop();
    }
}