package com.oitsjustjose.vtweaks.common.config;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;

public class PlayerTweakConfig {

    public static ArrayList<ItemStack> challengerMobDrops = new ArrayList<>();

    private static String CATEGORY_PLAYER_TWEAKS = "player tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_SWING_PARITY;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Player Tweaks").push(CATEGORY_PLAYER_TWEAKS);

        ENABLE_SWING_PARITY = COMMON_BUILDER.comment("Adds parity to Player arm swings (inspired by snapshot 19W42A)")
                .define("enableArmSwingParity", true);

        COMMON_BUILDER.pop();
    }
}