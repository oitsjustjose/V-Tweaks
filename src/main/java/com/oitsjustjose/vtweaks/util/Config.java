package com.oitsjustjose.vtweaks.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	public Configuration config;
	public ConfigCategory MobTweaks;
	public ConfigCategory Enchantments;
	public ConfigCategory BlockTweaks;
	public ConfigCategory ItemTweaks;
	public ConfigCategory MiscFeatures;

	// Mob Configs
	public boolean enablePetArmory;
	public boolean enableMobDropBuffsChickens;
	public boolean enableMobDropBuffsCows;
	public boolean enableMobDropBuffsSkeletons;
	public boolean enableMobDropBuffsSquids;
	public boolean enableMobDropBuffsEndermen;
	public boolean enableHorseGlue;
	public boolean enableFeatherPlucker;
	public boolean enableChallengerMobs;
	public String[] challengerMobDefaultLoot = new String[] { "minecraft:gold_ingot", "minecraft:gold_nugget*15", "minecraft:diamond", "minecraft:emerald", "minecraft:ghast_tear", "minecraft:ender_pearl", "minecraft:emerald", "minecraft:experience_bottle", "minecraft:record_13", "minecraft:record_cat", "minecraft:record_blocks", "minecraft:record_chirp", "minecraft:record_far", "minecraft:record_mall", "minecraft:record_mellohi", "minecraft:record_stal", "minecraft:record_strad", "minecraft:record_ward", "minecraft:record_11", "minecraft:record_wait" };
	public String[] challengerMobLoot;
	public int challengerMobRarity;
	public boolean disableBats;
	public boolean disablePigZombies;
	public boolean disableWitherOverworld;
	public boolean enableSheepDyeFix;
	public String[] sheepDyeDefaultBlacklist = new String[] { "net.minecraft.item.ItemDye", "biomesoplenty" };
	public String[] sheepDyeBlacklist;

	// Enchantment Configs
	public int hypermendingID;
	public int autosmeltID;
	public int stepboostID;
	public int lumberingID;
	public boolean enableAutosmeltFortuneInteraction;
	public String[] autosmeltOverrides;
	public int hypermendingXPCost;
	public int autosmeltXPCost;
	public int stepboostXPCost;
	public int lumberingXPCost;
	public boolean enableFeatherFallingTweak;
	public boolean enableDisenchantRecipes;

	// Block Configs
	public boolean enableCropHelper;
	public String[] cropHelperBlacklistDefaults = new String[] { "harvestcraft", "tehnut.resourceful.crops" };
	public String[] cropHelperBlacklist;
	public boolean enableBonemealTweaks;
	public boolean enableCakeTweak;
	public boolean enableToolEffTweaks;
	public boolean enableTorchHelper;
	public boolean enableHangingItemFix;
	public boolean enableDropTweaksSaplings;
	public boolean enableLavaLossPrevention;

	// Item Configs
	public boolean enableLeafEater;
	public boolean leafEaterReqSneak;
	public boolean enableWoodItemFuelHandler;
	public boolean enableDropTweaksDespawn;
	public int enableNewDespawnTime;
	public boolean enableDropTweaksEggHatching;
	public int enableEggHatchChance;
	public boolean enableSleepingBags;

	// Misc Configs
	public boolean enableRecipeHorseArmor;
	public boolean enableStackTweaks;
	public boolean enableStormTweak;
	public boolean enablePingProtection;
	public boolean enableDeathPoint;
	public int foodTooltipSetting;
	public int durabilityTooltipSetting;
	public boolean enableGuideNotifier;

	public ArrayList<ItemStack> challengerLootTable;

	public Config(File configFile)
	{
		this.init(configFile);
	}

	void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	public void setChallengerLootTable(ArrayList<ItemStack> newList)
	{
		this.challengerLootTable = newList;
	}

	void loadConfiguration()
	{
		Property property;

		// Mob Tweaks
		String category = "mob tweaks";
		List<String> propertyOrder = Lists.newArrayList();
		MobTweaks = config.getCategory(category);
		MobTweaks.setComment("Various Tweaks to Mobs");

		property = config.get(category, "Allow pet armor", true);
		property.setComment("Allows you to R-Click on TAMED pets with horse armor to armor them up!");
		enablePetArmory = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Chickens Drop Extra Feathers", true);
		property.setComment("If set to false, chicken drops will be unchanged");
		enableMobDropBuffsChickens = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Cows Drop Extra Leather", true);
		property.setComment("If set to false, cow drops will be unchanged");
		enableMobDropBuffsCows = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Skeletons Drop Extra Bones", true);
		property.setComment("If set to false, skeleton drops will be unchanged");
		enableMobDropBuffsSkeletons = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Squids Drop Extra Ink Sacs", true);
		property.setComment("If set to false, squid drops will be unchanged");
		enableMobDropBuffsSquids = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Endermen Drop Extra Ender Pearls", true);
		property.setComment("If set to false, enderman drops will be unchanged");
		enableMobDropBuffsEndermen = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Horses drop \"glue\" rarely with a Fire Aspect weapon", true);
		property.setComment("If set to false, horse drops will be unchanged");
		enableHorseGlue = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Pluck Feathers from Chickens", true).setRequiresMcRestart(true);
		property.setComment("Allows chickens to have a feather plucked using shears");
		enableFeatherPlucker = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Bats", false);
		property.setComment("Disables all bat spawns from the world. May prevent irration; side-effects are: difficulties with angel ring acquisition from ExU2");
		disableBats = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Pig Zombies", false);
		property.setComment("Also balances Ghast spawns");
		disablePigZombies = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disallow Wither Spawning in the Overworld", false);
		property.setComment("Special request - only allows the wither to be summoned in non-overworld dimensions");
		disableWitherOverworld = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Fix Ore Dictionary dyes not working to color sheep fleece", true);
		property.setComment("Enabling this feature lets you use any Ore Dictionary registered dye to recolor sheep");
		enableSheepDyeFix = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Ignored Dyes", sheepDyeDefaultBlacklist, "The class name (or part of it) of the dye you don't want to work with the Sheep Dye Fix");
		sheepDyeBlacklist = property.getStringList();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Enabled", true);
		property.setComment("Randomly spawns more difficult (but more lootworthy) enemies Applies to ALL enemies");
		enableChallengerMobs = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Rarity", 75, "There is a 1 in 'x' chance for Challenger mobs to spawn, this is 'x'", 1, Short.MAX_VALUE);
		challengerMobRarity = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Loot Table", challengerMobDefaultLoot, "Loot table. Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>").setRequiresMcRestart(true);
		challengerMobLoot = property.getStringList();
		propertyOrder.add(property.getName());

		MobTweaks.setPropertyOrder(propertyOrder);

		// Enchantments
		category = "enchantments";
		propertyOrder = Lists.newArrayList();
		Enchantments = config.getCategory(category);
		Enchantments.setComment("Enchantment ID's and Tweaks");

		property = config.get(category, "Hypermending Enchantment ID", 233, "If set to 0, the enchantment is disabled", 0, 255);
		hypermendingID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autosmelt Enchantment ID", 234, "If set to 0, the enchantment is disabled", 0, 255);
		autosmeltID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Step Boost Enchantment ID", 235, "If set to 0, the enchantment is disabled", 0, 255);
		stepboostID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Lumbering Enchantment ID", 236, "If set to 0, the enchantment is disabled", 0, 255);
		lumberingID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Autosmelt Fortune Interaction", true);
		property.setComment("Setting this to false will completely disable fortune bonuses on Autosmelted blocks");
		enableAutosmeltFortuneInteraction = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autosmelt Fortune Interaction Overrides", new String[] {});
		property.setComment("Registry Names (or part of a registry name) that you want to have Autosmelt + Fortune interaction");
		autosmeltOverrides = property.getStringList();
		propertyOrder.add(property.getName());

		property = config.get(category, "Hypermending XP Cost", 30, "The number of levels that crafting this book will require.", 1, 40);
		hypermendingXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Auto-Smelt XP Cost", 15, "The number of levels that crafting this book will require.", 1, 40);
		autosmeltXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "StepBoost XP Cost", 5, "The number of levels that crafting this book will require.", 1, 40);
		stepboostXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Lumbering XP Cost", 20, "The number of levels that crafting this book will require.", 1, 40);
		lumberingXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Better Feather Falling", true);
		property.setComment("Tweaks Feather Falling IV to negate ALL fall damage");
		enableFeatherFallingTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Item Disenchantment", true).setRequiresMcRestart(true);
		property.setComment("Allow crafting a piece of paper with an enchanted tool to disenchant said tool");
		enableDisenchantRecipes = property.getBoolean();
		propertyOrder.add(property.getName());

		Enchantments.setPropertyOrder(propertyOrder);

		// Block Tweaks
		category = "block tweaks";
		propertyOrder = Lists.newArrayList();
		BlockTweaks = config.getCategory(category);
		BlockTweaks.setComment("Tweaks for Blocks");

		property = config.get(category, "Easy Crop Harvesting", true);
		property.setComment("Allows for right-click-to-harvest on nearly any (including mod) crop");
		enableCropHelper = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Easy Crop Harvesting Class Blacklist", cropHelperBlacklistDefaults);
		property.setComment("Objects listed here will not be effected by the Easy Crop Harvesting tweak. These are class names, or parts of class names");
		cropHelperBlacklist = property.getStringList();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Bonemeal Tweak", true);
		property.setComment("Allows more things to be bonemealed");
		enableBonemealTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Cake Drops", true);
		property.setComment("Uneaten Cakes can be broken and re-acquired");
		enableCakeTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Efficiency Tweaks", true);
		property.setComment("Fixes some tools NOT being effective on certain materials");
		enableToolEffTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Torch Placement", true);
		property.setComment("Right clicking with a tool will place a torch from your inventory");
		enableTorchHelper = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Glitching Item Fix", true);
		property.setComment("Fixes common vanilla instances of items spawning and glitching everywhere by bypassing the spawning situation completely");
		enableHangingItemFix = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Sapling Self-Planting", true);
		property.setComment("Makes it so that any saplings that fall will automatically plant themselves if they can");
		enableDropTweaksSaplings = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Lava Loss Prevention", true);
		property.setComment("Enabling this feature helps prevent Obsidian (and Chisel's Basalt) from being burnt by lava");
		enableLavaLossPrevention = property.getBoolean();
		propertyOrder.add(property.getName());

		BlockTweaks.setPropertyOrder(propertyOrder);

		// Item Tweaks
		category = "item tweaks";
		propertyOrder = Lists.newArrayList();
		ItemTweaks = config.getCategory(category);
		ItemTweaks.setComment("Tweaks for Items");

		property = config.get(category, "Axes destroy leaves in a wider area", true);
		property.setComment("If enabled, when an axe is used to break leaves, many in the area are destroyed");
		enableLeafEater = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Require sneaking", false);
		property.setComment("If above tweak is enabled, you must sneak to enable the AOE effect");
		leafEaterReqSneak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Add Missing Items as Fuels", true).setRequiresMcRestart(true);
		property.setComment("Adds wooden items to fuel list if they were missing");
		enableWoodItemFuelHandler = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Adjust Despawn Timer for Items Dropped in the World", false);
		property.setComment("Allows for items' despawn timer to be longer or shorter, depending on what your preference is");
		enableDropTweaksDespawn = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "New Despawn Timer for Items (if enabled)", 6000, "If the above option is enabled, here you can control how long dropped items last in the world before despawning. -1 means items don't despawn", -1, Integer.MAX_VALUE);
		enableNewDespawnTime = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Allow Despawning Eggs to Randomly Hatch", false);
		property.setComment("If enabled, when eggs are about to despawn they will have a chance to hatch into a baby chick!");
		enableDropTweaksEggHatching = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Chance for Eggs to Hatch", 64, "There is a 1 in 'x' chance for an Egg to hatch into a chick, where 'x' is this value", 1, Integer.MAX_VALUE);
		enableEggHatchChance = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Pocket Bed Tweak", true);
		property.setComment("If enabled, naming a bed 'Sleeping Bag' and right clicking it makes it work like a sleeping bag");
		enableSleepingBags = property.getBoolean();
		propertyOrder.add(property.getName());

		ItemTweaks.setPropertyOrder(propertyOrder);

		// Misc Features
		category = "miscellaneous";
		propertyOrder = Lists.newArrayList();
		MiscFeatures = config.getCategory(category);
		MiscFeatures.setComment("Other Tweaks");

		property = config.get(category, "Enable Horse Armor Recipes", true);
		property.setComment("Combining two pairs of undamaged leggings in an anvil will get you horse armor of that type");
		enableRecipeHorseArmor = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Stack Size Tweaks", true).setRequiresMcRestart(true);
		property.setComment("Adjusts Max Stack Sizes of some vanilla items");
		enableStackTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Lightning", true);
		property.setComment("Disables lightning from spawning, it can get annoying");
		enableStormTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Ping Protection", false);
		property.setComment("This option lowers the amount of Entity damage taken by players with a bad multiplayer connection. The worse the connection, the less damage they will take");
		enablePingProtection = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Death Point Message", true);
		property.setComment("Enabling this feature makes it so a chat message appears notifying the player of where they died");
		enableDeathPoint = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Food Value Tooltips", 2, "0 disables the feature, 1 enables the features all the time, 2 enables the feature only while sneaking", 0, 2);
		foodTooltipSetting = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Durability Tooltips", 2, "0 disables the feature, 1 enables the features all the time, 2 enables the feature only while sneaking", 0, 2);
		durabilityTooltipSetting = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Welcome Message", true);
		property.setComment("If enabled, a nice chat message is shown once per world with a link to the Wiki");
		enableGuideNotifier = property.getBoolean();
		propertyOrder.add(property.getName());

		MiscFeatures.setPropertyOrder(propertyOrder);

		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public void update(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(VTweaks.MODID))
			loadConfiguration();
	}
}