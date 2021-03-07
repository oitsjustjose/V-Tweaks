package com.oitsjustjose.vtweaks.common.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class MobTweakConfig {
    private static String CATEGORY_MOB_TWEAKS = "mob tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_PET_ARMORY;
    public static ForgeConfigSpec.EnumValue<NoPetFriendlyFire> NO_PET_FRIENDLY_FIRE;
    public static ForgeConfigSpec.BooleanValue ENABLE_SMALL_BEES;
    public static ForgeConfigSpec.BooleanValue ENABLE_FEATHER_PLUCKING;
    public static ForgeConfigSpec.BooleanValue ENABLE_CHALLENGER_MOBS;
    public static ForgeConfigSpec.BooleanValue ENABLE_CHALLENGER_MOBS_NAME;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CHALLENGER_MOBS_LOOT;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> CHALLENGER_MOBS_BLACKLIST;
    public static ForgeConfigSpec.IntValue CHALLENGER_MOBS_RARITY;
    public static ForgeConfigSpec.BooleanValue ENABLE_PEACEFUL_SURFACE;
    public static ForgeConfigSpec.IntValue PEACEFUL_SURFACE_MIN_Y;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> PEACEFUL_SURFACE_BLACKLIST;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Mob Tweaks").push(CATEGORY_MOB_TWEAKS);

        ENABLE_PET_ARMORY = COMMON_BUILDER.comment("Allows you to gear up tamed pets any armor and/or weapon. Doesn't render, but DOES work!")
                .define("enablePetArmory", true);
        NO_PET_FRIENDLY_FIRE = COMMON_BUILDER.comment(
                "If set to \"OWNER\", this will prevent owners of pets from attacking their own pet. If set to \"ALL\", this prevents all players from attacking anyone's pet")
                .defineEnum("disablePetFriendlyFire", NoPetFriendlyFire.OWNER);
        ENABLE_SMALL_BEES = COMMON_BUILDER
                .comment("If enabled, all bees will always be half the size they are in Vanilla. Does not affect breeding or hitboxes")
                .define("enableSmallBees", true);
        ENABLE_FEATHER_PLUCKING = COMMON_BUILDER.comment("Allows chicken feathers to be plucked w/ shears")
                .define("enableFeatherPlucking", true);
        ENABLE_CHALLENGER_MOBS = COMMON_BUILDER.comment(
                "Randomly spawns more difficult (but more lootworthy) enemies.\nApplies to ALL enemies but those in this blacklist.")
                .define("challengerMobsEnabled", true);
        ENABLE_CHALLENGER_MOBS_NAME = COMMON_BUILDER.comment("Enable custom nametags on challenger mobs")
                .define("challengerMobsNames", true);
        CHALLENGER_MOBS_LOOT = COMMON_BUILDER
                .comment("A string list of form <modid:item> of possible loot items a Challenger Mob could drop")
                .defineList("challengerMobsLoot",
                        Lists.newArrayList("minecraft:gold_ingot", "minecraft:gold_nugget*15", "minecraft:diamond",
                                "minecraft:emerald", "minecraft:ghast_tear", "minecraft:ender_pearl",
                                "minecraft:emerald", "minecraft:experience_bottle"),
                        (itemRaw) -> itemRaw instanceof String);
        CHALLENGER_MOBS_BLACKLIST = COMMON_BUILDER
                .comment("The class name (or part of it) of any entities that should not be turned to challenger mobs")
                .defineList("challengerMobsBlacklist", Lists.newArrayList("minecraft:pillager"), (itemRaw) -> {
                    if (itemRaw instanceof String) {
                        return true;
                    }
                    return false;
                });
        CHALLENGER_MOBS_RARITY = COMMON_BUILDER.comment(
                "The frequency (out of 100 - higher means more frequent) of a mob being turned into a Challenger")
                .defineInRange("challengerMobsFrequency", 15, 1, 100);
        ENABLE_PEACEFUL_SURFACE = COMMON_BUILDER
                .comment("Prevents mobs from spawning above sea level unless it's a new moon")
                .define("peacefulSurfaceEnabled", false);
        PEACEFUL_SURFACE_MIN_Y = COMMON_BUILDER.comment("The lowest Y-level which mobs will be prevented from spawning")
                .defineInRange("peacefulSurfaceMinY", 60, 1, 255);
        PEACEFUL_SURFACE_BLACKLIST = COMMON_BUILDER
                .comment("A list of dimensions (of form <modid:type>) to ignore when prevent surface mob spawns.")
                .defineList("peacefulSurfaceDimBlacklist",
                        Lists.newArrayList("minecraft:the_nether", "minecraft:the_end"), (itemRaw) -> {
                            if (itemRaw instanceof String) {
                                return true;
                            }
                            return false;
                        });

        COMMON_BUILDER.pop();
    }

    public enum NoPetFriendlyFire {
        DISABLED, OWNER, ALL
    }
}