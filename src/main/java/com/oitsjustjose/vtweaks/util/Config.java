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
	public boolean petArmory;
	public boolean featherBuff;
	public boolean hideBuff;
	public boolean boneBuff;
	public boolean sacBuff;
	public boolean enderpearlBuff;
	public boolean pluckFeather;
	public boolean challengers;
	final String[] CHALLENGER_MOB_DEFAULTS = new String[] { "Tanky", "Hungry", "Ranger", "Mage", "Pyro", "Zestonian", "Resilient", "Hyper" };
	public String[] CHALLENGER_MOB_LOOT_TABLE_DEFAULTS = new String[] { "minecraft:gold_ingot", "minecraft:gold_nugget*15", "minecraft:diamond", "minecraft:emerald", "minecraft:ghast_tear", "minecraft:ender_pearl", "minecraft:emerald", "minecraft:experience_bottle", "minecraft:record_13", "minecraft:record_cat", "minecraft:record_blocks", "minecraft:record_chirp", "minecraft:record_far", "minecraft:record_mall", "minecraft:record_mellohi", "minecraft:record_stal", "minecraft:record_strad", "minecraft:record_ward", "minecraft:record_11", "minecraft:record_wait" };
	public String[] challengerMobs;
	public String[] challengerMobLootTable;
	public int challengerMobRarity;
	public boolean noBats;
	public boolean noPigZombies;
	public boolean noOverworldWither;

	// Enchantment Configs
	public int hypermendingID;
	public int autosmeltID;
	public int stepboostID;
	public int lumberingID;
	public int piercingID;
	public int dwarvenLuckID;
	public String[] autosmeltOverrides;
	public int hypermendingXPCost;
	public int autosmeltXPCost;
	public int stepboostXPCost;
	public int lumberingXPCost;
	public boolean featherFalling;
	public boolean disenchant;
	// Block Configs
	public boolean cropHarvest;
	public boolean bonemealTweak;
	public boolean cakeTweak;
	public boolean toolEffTweaks;
	public boolean torchPlacer;
	public boolean glitchingItemFix;
	// Item Configs
	public boolean addFuels;
	// Misc Configs
	public boolean earlyGame;
	public boolean horseArmor;
	public boolean stackSizeTweaks;
	public boolean lightning;
	public int foodToolTips;
	public int durabilityToolTips;

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

		property = config.get(category, "Allow pet armor", true).setRequiresMcRestart(true);
		property.setComment("Allows you to R-Click on TAMED pets with horse armor to armor them up!");
		petArmory = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Chickens Drop Extra Feathers", true).setRequiresMcRestart(true);
		property.setComment("If set to false, chicken drops will be unchanged");
		featherBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Cows Drop Extra Leather", true).setRequiresMcRestart(true);
		property.setComment("If set to false, cow drops will be unchanged");
		hideBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Skeletons Drop Extra Bones", true).setRequiresMcRestart(true);
		property.setComment("If set to false, skeleton drops will be unchanged");
		boneBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Squids Drop Extra Ink Sacs", true).setRequiresMcRestart(true);
		property.setComment("If set to false, squid drops will be unchanged");
		sacBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Endermen Drop Extra Ender Pearls", true).setRequiresMcRestart(true);
		property.setComment("If set to false, enderman drops will be unchanged");
		enderpearlBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Pluck Feathers from Chickens", true).setRequiresMcRestart(true);
		property.setComment("Allows chickens to have a feather plucked using shears");
		pluckFeather = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Bats", false).setRequiresMcRestart(true);
		property.setComment("Disables all bat spawns from the world. May prevent irration; side-effects are: difficulties with angel ring acquisition from ExU2");
		noBats = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Pig Zombies", false).setRequiresMcRestart(true);
		property.setComment("Also balances Ghast spawns");
		noPigZombies = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disallow Wither Spawning in the Overworld", false).setRequiresMcRestart(true);
		property.setComment("Special request - only allows the wither to be summoned in non-overworld dimensions");
		noOverworldWither = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Enabled", true).setRequiresMcRestart(true);
		property.setComment("Randomly spawns more difficult (but more lootworthy) enemies Applies to ALL enemies");
		challengers = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Rarity", 75, "There is a 1 in 'x' chance for Challenger mobs to spawn, this is 'x'", 1, Short.MAX_VALUE).setRequiresMcRestart(true);
		challengerMobRarity = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Loot Table", CHALLENGER_MOB_LOOT_TABLE_DEFAULTS, "Loot table. Formatted as <modid>:<item>:<metadata>*<quantity>, <modid>:<item>*quantity, or <modid>:<item>").setRequiresMcRestart(true);
		challengerMobLootTable = property.getStringList();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs' Prefixes", CHALLENGER_MOB_DEFAULTS, "Renaming will not change anything, just their highlighted name").setRequiresMcRestart(false);
		challengerMobs = property.getStringList();
		propertyOrder.add(property.getName());

		MobTweaks.setPropertyOrder(propertyOrder);

		// Enchantments
		category = "enchantments";
		propertyOrder = Lists.newArrayList();
		Enchantments = config.getCategory(category);
		Enchantments.setComment("Enchantment ID's and Tweaks");

		property = config.get(category, "Hypermending Enchantment ID", 233, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		hypermendingID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autosmelt Enchantment ID", 234, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		autosmeltID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Step Boost Enchantment ID", 235, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		stepboostID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Lumbering Enchantment ID", 236, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		lumberingID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Piercing Enchantment ID", 237, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		piercingID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Dwarven Luck Enchantment ID", 238, "If set to 0, the enchantment is disabled", 0, 255).setRequiresMcRestart(true);
		dwarvenLuckID = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Autosmelt Fortune Interaction Overrides", new String[] {});
		property.setComment("Registry Names (or part of a registry name) that you want to have Autosmelt + Fortune interaction");
		autosmeltOverrides = property.getStringList();
		propertyOrder.add(property.getName());

		property = config.get(category, "Hypermending XP Cost", 30, "The number of levels that crafting this book will require.", 1, 40).setRequiresMcRestart(true);
		hypermendingXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Auto-Smelt XP Cost", 15, "The number of levels that crafting this book will require.", 1, 40).setRequiresMcRestart(true);
		autosmeltXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "StepBoost XP Cost", 5, "The number of levels that crafting this book will require.", 1, 40).setRequiresMcRestart(true);
		stepboostXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Lumbering XP Cost", 20, "The number of levels that crafting this book will require.", 1, 40).setRequiresMcRestart(true);
		lumberingXPCost = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Better Feather Falling", true).setRequiresMcRestart(true);
		property.setComment("Tweaks Feather Falling IV to negate ALL fall damage");
		featherFalling = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Item Disenchantment", true).setRequiresMcRestart(true);
		property.setComment("Allow crafting a piece of paper with an enchanted tool to disenchant said tool");
		disenchant = property.getBoolean();
		propertyOrder.add(property.getName());

		Enchantments.setPropertyOrder(propertyOrder);

		// Block Tweaks
		category = "block tweaks";
		propertyOrder = Lists.newArrayList();
		BlockTweaks = config.getCategory(category);
		BlockTweaks.setComment("Tweaks for Blocks");

		property = config.get(category, "Easy Crop Harvesting", true).setRequiresMcRestart(true);
		property.setComment("Allows for right-click-to-harvest on nearly any (including mod) crop. No seeds will be dropped - intended");
		cropHarvest = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Bonemeal Tweak", true).setRequiresMcRestart(true);
		property.setComment("Allows more things to be bonemealed; Nether Wart requires Blaze Powder");
		bonemealTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Cake Drops", true).setRequiresMcRestart(true);
		property.setComment("Uneaten Cakes can be broken and re-acquired");
		cakeTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Efficiency Tweaks", true).setRequiresMcRestart(true);
		property.setComment("Fixes some tools NOT being effective on certain materials");
		toolEffTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Torch Placement", true).setRequiresMcRestart(true);
		property.setComment("Right clicking with a tool will place a torch from your inventory");
		torchPlacer = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Glitching Item Fix", true).setRequiresMcRestart(true);
		property.setComment("Fixes common vanilla instances of items spawning and glitching everywhere by bypassing the spawning situation completely");
		glitchingItemFix = property.getBoolean();
		propertyOrder.add(property.getName());

		BlockTweaks.setPropertyOrder(propertyOrder);

		// Item Tweaks
		category = "item tweaks";
		propertyOrder = Lists.newArrayList();
		ItemTweaks = config.getCategory(category);
		ItemTweaks.setComment("Tweaks for Items");

		property = config.get(category, "Add Missing Items as Fuels", true).setRequiresMcRestart(true);
		property.setComment("Adds wooden items to fuel list if they were missing");
		addFuels = property.getBoolean();
		propertyOrder.add(property.getName());

		ItemTweaks.setPropertyOrder(propertyOrder);

		// Misc Features
		category = "miscellaneous";
		propertyOrder = Lists.newArrayList();
		MiscFeatures = config.getCategory(category);
		MiscFeatures.setComment("Other Tweaks");

		property = config.get(category, "Change Base Game Mechanics", false).setRequiresMcRestart(true);
		property.setComment("This config allows for flint and gravel to be a reasonably heavy part of crafting / early-game gameplay");
		earlyGame = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Horse Armor Recipes", true).setRequiresMcRestart(true);
		property.setComment("Combining two pairs of undamaged leggings in an anvil will get you horse armor of that type");
		horseArmor = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Stack Size Tweaks", true).setRequiresMcRestart(true);
		property.setComment("Adjusts Max Stack Sizes of some vanilla items");
		stackSizeTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Lightning", true).setRequiresMcRestart(true);
		property.setComment("Disables lightning from spawning, it can get annoying");
		lightning = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Food Value Tooltips", 2, "0 disables the feature, 1 enables the feature all the time, 2 enables the feature only while sneaking", 0, 2).setRequiresMcRestart(false).setRequiresWorldRestart(false);
		foodToolTips = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Durability Tooltips", 2, "0 disables the feature, 1 enables the feature all the time, 2 enables the feature only while sneaking", 0, 2).setRequiresMcRestart(false).setRequiresWorldRestart(false);
		durabilityToolTips = property.getInt();
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