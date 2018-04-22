package com.oitsjustjose.vtweaks.util;


import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Config(modid = VTweaks.MODID)
public class ModConfig
{
    @Config.Name("Enchantments")
    public static Enchantments enchantments = new Enchantments();

    @Config.Name("Mob Tweaks")
    public static MobTweaks mobTweaks = new MobTweaks();

    @Config.Name("Block Tweaks")
    public static BlockTweaks blockTweaks = new BlockTweaks();

    @Config.Name("Item Tweaks")
    public static ItemTweaks itemTweaks = new ItemTweaks();

    @Config.Name("Miscellaneous")
    public static Misc misc = new Misc();

    public static class Enchantments
    {
        @Config.Name("Enable Lumbering")
        public boolean enableLumbering = true;

        @Config.Name("Enable Imperishable")
        public boolean enableImperishable = true;

        @Config.Name("Enable Feather Falling Tweak")
        @Config.Comment("Feather Falling IV or above directs 100% of fall damage to boots")
        public boolean enableFeatherFallTweak = true;

        @Config.Name("Lumbering Recipe XP Cost")
        @Config.RangeInt(min = 1, max = 40)
        public int lumberingCost = 16;
    }

    public static class MobTweaks
    {
        @Config.Name("Enable Pet Armory")
        @Config.Comment("Allows you to R-Click your tamed pets with horse armor")
        public boolean enablePetArmory = true;

        @Config.Name("Drop Buffs")
        @Config.Comment("Additional drops to various mobs")
        public DropBuffs dropBuffs = new DropBuffs();

        @Config.Name("Enable \"glue\" drops")
        @Config.Comment("Changes horse drops under \"hot\" conditions")
        public boolean enableHorseGlue = true;

        @Config.Name("Enable Feather Plucking")
        @Config.Comment("Allows chicken feathers to be plucked w/ shears")
        public boolean enableFeatherPlucking = true;

        @Config.Name("Sheep Dye Fix")
        public SheepDyeFix sheepDyeFix = new SheepDyeFix();

        @Config.Name("Challenger Mobs")
        public ChallengerMobs challengerMobs = new ChallengerMobs();


        public static class DropBuffs
        {
            @Config.Name("Chicken Feathers")
            public boolean chickenFeathers = true;

            @Config.Name("Cow Leather")
            public boolean cowLeather = true;

            @Config.Name("Skeleton Bones")
            public boolean skeletonBones = true;

            @Config.Name("Squid Ink Sacs")
            public boolean squidSacs = true;

            @Config.Name("Enderman Ender Pearls")
            public boolean enderPearls = true;
        }

        public static class SheepDyeFix
        {
            @Config.Name("Enable Tweak")
            @Config.Comment("Allows sheep to be dyed with any oreDict dye")
            public boolean enabled = true;

            @Config.Name("Blacklisted Dyes")
            @Config.Comment("The class name (or part of it) of the dye you don't want to work with the Sheep Dye Fix")
            public String[] blacklist = new String[]{"net.minecraft.item.ItemDye", "biomesoplenty"};
        }

        public static class ChallengerMobs
        {
            public static ArrayList<ItemStack> challengerLootTable = new ArrayList<>();

            @Config.Name("Enable Tweak")
            @Config.Comment("Randomly spawns more difficult (but more lootworthy) enemies Applies to ALL enemies")
            public boolean enabled = true;

            @Config.Name("Rarity")
            @Config.Comment("")
            public int chance = 100;

            @Config.Name("Loot Table")
            @Config.Comment("Loot table. Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>")
            public String[] loot = new String[]{
                    "minecraft:gold_ingot",
                    "minecraft:gold_nugget*15",
                    "minecraft:diamond",
                    "minecraft:emerald",
                    "minecraft:ghast_tear",
                    "minecraft:ender_pearl",
                    "minecraft:emerald",
                    "minecraft:experience_bottle"
            };

            @Config.Name("Entity Blacklist")
            @Config.Comment("The class name (or part of it) of any entities that should not be turned to challenger mobs")
            public String[] blacklist = new String[]{};
        }
    }

    public static class BlockTweaks
    {
        @Config.Name("Easy Crop Harvesting")
        @Config.Comment("Allows for right-click-to-harvest on nearly any (including mod) crop")
        public CropTweak cropTweak = new CropTweak();

        @Config.Name("Enable Bone Meal Tweak")
        @Config.Comment("Allows more plants to be bonemealed")
        public boolean enableBonemealTweak = true;

        @Config.Name("Enable Cake Drop if uneaten")
        public boolean enableCakeDrop = true;

        @Config.Name("Enable Effective Tool Tweaks")
        @Config.Comment("Fixes some tools NOT being effective on certain materials")
        public boolean enableToolEffTweaks = true;

        public static class CropTweak
        {
            @Config.Name("Enable Tweak")
            public boolean enableCropTweak = true;

            @Config.Name("Blacklisted Crops")
            @Config.Comment("Objects listed here will not be effected by the Easy Crop Harvesting tweak. These are class names, or parts of class names")
            public String[] blacklist = new String[]{"harvestcraft", "tehnut.resourceful.crops", "actuallyadditions"};
        }
    }

    public static class ItemTweaks
    {
        @Config.Name("Enable Egg Hatching")
        @Config.Comment("Allows egg items to hatch instead of despawn")
        public boolean enableEggHatching = true;

        @Config.Name("Egg Hatching Chance")
        @Config.Comment("One in X chance of an egg hatching, X dictated below:")
        @Config.RangeInt(min = 1)
        public int eggHatchingChance = 256;

        @Config.Name("Enable Sapling Self-Planting")
        public boolean enableSaplingPlanting = true;

        @Config.Name("Adjust Item Despawn Time (in ticks: 20 ticks in a second)")
        @Config.Comment("-1 disables any adjustments")
        @Config.RangeInt(min = -1)
        public int despawnTimeSetting = -1;

    }

    public static class Misc
    {
        @Config.Name("Enable Horse Armor Recipes")
        public boolean enableHorseArmorRecipes = true;

        @Config.Name("Disable Lightning In Storms")
        public boolean enableStormTweak = true;

        @Config.Name("Enable Death Point Message")
        public boolean enableDeathPoint = true;

        @Config.Name("Food Tooltips")
        @Config.Comment("0 disables, 1 shows always, 2 shows with SHIFT")
        @Config.RangeInt(min = 0, max = 2)
        public int foodTooltipSetting = 2;

        @Config.Name("Durability Tooltips")
        @Config.Comment("0 disables, 1 shows always, 2 shows with SHIFT")
        @Config.RangeInt(min = 0, max = 2)
        public int durabilityTooltipSetting = 2;

        @Config.Name("Enable Welcome Message")
        public boolean enableGuideNotifier = true;

    }


    @SubscribeEvent
    public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        System.out.println("Changed");
        if (event.getModID().equalsIgnoreCase(VTweaks.MODID))
        {
            ConfigManager.sync(VTweaks.MODID, Config.Type.INSTANCE);
            ConfigParser.parseItems();
        }
    }

    public static void setChallengerLootTable(ArrayList<ItemStack> newList)
    {
        MobTweaks.ChallengerMobs.challengerLootTable = new ArrayList<>();
        MobTweaks.ChallengerMobs.challengerLootTable.addAll(newList);
    }
}
