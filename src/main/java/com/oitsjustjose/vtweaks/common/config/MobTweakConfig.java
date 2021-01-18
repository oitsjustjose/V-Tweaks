package com.oitsjustjose.vtweaks.common.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

public class MobTweakConfig {

    public static ArrayList<ItemStack> challengerMobDrops = new ArrayList<>();

    private static String CATEGORY_MOB_TWEAKS = "mob tweaks";

    public static ForgeConfigSpec.BooleanValue ENABLE_PET_ARMORY;
    public static ForgeConfigSpec.BooleanValue ENABLE_PET_ARMORY_WEAPONS;
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

        ENABLE_PET_ARMORY = COMMON_BUILDER.comment("Allows you to R-Click your tamed pets with horse armor")
                .define("enablePetArmory", true);
        ENABLE_PET_ARMORY_WEAPONS = COMMON_BUILDER
                .comment("Enabling this allows tamed pets to pick up weapons (which do actually work but don't render)")
                .define("enablePetWeaponry", true);
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
                        (itemRaw) -> {
                            if (itemRaw instanceof String) {
                                String itemName = (String) itemRaw;
                                String[] parts = itemName.split("[\\W]");

                                if (parts.length != 2 && parts.length != 3) {
                                    return false;
                                }

                                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parts[0], parts[1]));

                                if (item != null) {
                                    challengerMobDrops.add(
                                            new ItemStack(item, parts.length == 3 ? Integer.parseInt(parts[2]) : 1));
                                    return true;
                                }
                            }
                            return false;
                        });
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
}