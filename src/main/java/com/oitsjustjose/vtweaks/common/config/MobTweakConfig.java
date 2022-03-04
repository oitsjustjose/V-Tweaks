package com.oitsjustjose.vtweaks.common.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;

public class MobTweakConfig {
    private static final String CATEGORY_MOB_TWEAKS = "mob tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_PET_ARMORY;
    public static ForgeConfigSpec.EnumValue<NoPetFriendlyFire> NO_PET_FRIENDLY_FIRE;
    public static ForgeConfigSpec.BooleanValue ENABLE_FEATHER_PLUCKING;
    public static ForgeConfigSpec.BooleanValue ENABLE_CHALLENGER_MOBS;
    public static ForgeConfigSpec.DoubleValue GLOBAL_CHALLENGER_MOB_CHANCE;
    public static ForgeConfigSpec.BooleanValue ENABLE_PEACEFUL_SURFACE;
    public static ForgeConfigSpec.IntValue PEACEFUL_SURFACE_MIN_Y;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> PEACEFUL_SURFACE_BLACKLIST;
    public static ForgeConfigSpec.BooleanValue DISABLE_BABY_ZOMBIES;

    public static void init(ForgeConfigSpec.Builder COMMON_BUILDER) {
        COMMON_BUILDER.comment("Mob Tweaks").push(CATEGORY_MOB_TWEAKS);

        ENABLE_PET_ARMORY = COMMON_BUILDER.comment("Allows you to gear up tamed pets any armor and/or weapon. Doesn't render, but DOES work!")
                .define("enablePetArmory", true);
        NO_PET_FRIENDLY_FIRE = COMMON_BUILDER.comment(
                        "If set to \"OWNER\", this will prevent owners of pets from attacking their own pet. If set to \"ALL\", this prevents all players from attacking anyone's pet")
                .defineEnum("disablePetFriendlyFire", NoPetFriendlyFire.OWNER);
        ENABLE_FEATHER_PLUCKING = COMMON_BUILDER.comment("Allows chicken feathers to be plucked w/ shears")
                .define("enableFeatherPlucking", true);
        ENABLE_CHALLENGER_MOBS = COMMON_BUILDER.comment(
                        "A data-driven way to make some special mobs with abilities, effects, specialized loot and more!")
                .define("challengerMobsEnabled", true);
        GLOBAL_CHALLENGER_MOB_CHANCE = COMMON_BUILDER
                .comment("This controls the overall chance for V-Tweaks to attempt converting a monster to a Challenger.\nThis chance is applied before any Challenger Mob weights or entity filters.")
                .defineInRange("challengerMobGlobalChance", 0.25D, 0.0D, 1.0D);
        ENABLE_PEACEFUL_SURFACE = COMMON_BUILDER
                .comment("Prevents mobs from spawning above sea level unless it's a new moon")
                .define("peacefulSurfaceEnabled", false);
        PEACEFUL_SURFACE_MIN_Y = COMMON_BUILDER.comment("The lowest Y-level which mobs will be prevented from spawning")
                .defineInRange("peacefulSurfaceMinY", 60, 1, 255);
        PEACEFUL_SURFACE_BLACKLIST = COMMON_BUILDER
                .comment("A list of dimensions (of form <modid:type>) to ignore when prevent surface mob spawns.")
                .defineList("peacefulSurfaceDimBlacklist",
                        Lists.newArrayList("minecraft:the_nether", "minecraft:the_end"), (itemRaw) -> itemRaw instanceof String);
        DISABLE_BABY_ZOMBIES = COMMON_BUILDER.comment("Any time a Baby Zombie (or Zombie Piglin) spawns, it will be replaced with an adult").define("disableBabyZombies", true);

        COMMON_BUILDER.pop();
    }

    public enum NoPetFriendlyFire {
        DISABLED, OWNER, ALL
    }
}