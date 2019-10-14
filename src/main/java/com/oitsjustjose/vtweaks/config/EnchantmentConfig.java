package com.oitsjustjose.vtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantmentConfig
{

    private static String CATEGORY_ENCHANTMENTS = "enchantments";

    public static ForgeConfigSpec.BooleanValue ENABLE_LUMBERING;
    public static ForgeConfigSpec.BooleanValue ENABLE_IMPERISHABLE;
    public static ForgeConfigSpec.BooleanValue ENABLE_FF_TWEAK;
    public static ForgeConfigSpec.IntValue LUMBERING_RECIPE_COST;
    public static ForgeConfigSpec.BooleanValue LUMBERING_CUTS_LEAVES;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER)
    {
        COMMON_BUILDER.comment("Enchantments").push(CATEGORY_ENCHANTMENTS);

        ENABLE_LUMBERING = COMMON_BUILDER.comment("Enable the Lumbering enchantment for Axes").define("enableLumbering",
                true);
        ENABLE_IMPERISHABLE = COMMON_BUILDER.comment("Enable the Imperishable enchantment for any breakable item")
                .define("enableImperishable", true);
        ENABLE_FF_TWEAK = COMMON_BUILDER.comment("Feather Falling IV or above directs 100% of fall damage to boots")
                .define("enableFeatherFallTweak", true);
        LUMBERING_RECIPE_COST = COMMON_BUILDER.comment("Number of levels Lumbering costs to create in an anvil")
                .defineInRange("lumberingCost", 16, 1, 40);
        LUMBERING_CUTS_LEAVES = COMMON_BUILDER
                .comment("Should the lumbering enchantments also prune the leaves of a tree?")
                .define("lumberingCutsLeaves", true);

        COMMON_BUILDER.pop();
    }
}