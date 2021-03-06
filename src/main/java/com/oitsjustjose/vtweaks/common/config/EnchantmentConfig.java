package com.oitsjustjose.vtweaks.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EnchantmentConfig {

    private static final String CATEGORY_ENCHANTMENTS = "enchantments";

    public static ForgeConfigSpec.BooleanValue ENABLE_LUMBERING;
    public static ForgeConfigSpec.IntValue LUMBERING_MAX_INT;
    public static ForgeConfigSpec.BooleanValue ENABLE_IMPERISHABLE;
    public static ForgeConfigSpec.BooleanValue ENABLE_FF_TWEAK;
    public static ForgeConfigSpec.IntValue IMPERISHABLE_RECIPE_COST;
    public static ForgeConfigSpec.IntValue LUMBERING_RECIPE_COST;
    public static ForgeConfigSpec.BooleanValue LUMBERING_CUTS_LEAVES;
    public static ForgeConfigSpec.BooleanValue LUMBERING_WOOD_STRICT;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Enchantments").push(CATEGORY_ENCHANTMENTS);

        ENABLE_LUMBERING = COMMON_BUILDER.comment("Enable the Lumbering enchantment for Axes").define("enableLumbering",
                true);
        LUMBERING_MAX_INT = COMMON_BUILDER.comment("The maximum amount of logs lumbering can destroy")
                .defineInRange("maxLumberCount", 1024, 1, Integer.MAX_VALUE);
        ENABLE_IMPERISHABLE = COMMON_BUILDER.comment("Enable the Imperishable enchantment for any breakable item")
                .define("enableImperishable", true);
        ENABLE_FF_TWEAK = COMMON_BUILDER.comment("Feather Falling IV or above directs 100% of fall damage to boots")
                .define("enableFeatherFallTweak", true);
        IMPERISHABLE_RECIPE_COST = COMMON_BUILDER.comment("Number of levels Imperishable costs to create in an anvil")
                .defineInRange("imperishableCost", 30, 1, 40);
        LUMBERING_RECIPE_COST = COMMON_BUILDER.comment("Number of levels Lumbering costs to create in an anvil")
                .defineInRange("lumberingCost", 16, 1, 40);
        LUMBERING_CUTS_LEAVES = COMMON_BUILDER
                .comment("Should the lumbering enchantment also prune the leaves of a tree?")
                .define("lumberingCutsLeaves", true);
        LUMBERING_WOOD_STRICT = COMMON_BUILDER.comment(
                "Should the lumbering enchantment be strict about wood types?\ne.g. if you're in a forest with mixed woods touching, should lumbering only break the same type of wood that was initially broken?")
                .define("lumberingIsStrictAboutWood", true);

        COMMON_BUILDER.pop();
    }
}