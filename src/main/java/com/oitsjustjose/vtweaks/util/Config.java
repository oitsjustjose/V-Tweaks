package com.oitsjustjose.vtweaks.util;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	public static Configuration config;
	static ConfigCategory MobTweaks;
	static ConfigCategory Enchantments;
	static ConfigCategory BlockTweaks;
	static ConfigCategory ItemTweaks;
	static ConfigCategory MiscFeatures;

	// Mob Configs
	public static boolean petArmory;
	public static boolean featherBuff;
	public static boolean hideBuff;
	public static boolean boneBuff;
	public static boolean sacBuff;
	public static boolean enderpearlBuff;
	public static boolean pluckFeather;
	public static boolean challengers;
	public static String[] challengerMobDefaults = new String[] { "Tanky", "Hungry", "Ranger", "Mage", "Pyro", "Zestonian", "Resilient", "Hyper" };
	public static String[] challengerMobs;
	public static int challengerMobRarity;
	public static boolean noBats;
	public static boolean noPigZombies;
	public static boolean noOverworldWither;
	public static boolean silenceVillagers;
	// Enchantment Configs
	public static int hypermendingID;
	public static int autosmeltID;
	public static int stepboostID;
	public static int lumberingID;
	public static boolean featherFalling;
	public static boolean disenchant;
	// Block Configs
	public static boolean cropHarvest;
	public static boolean bonemealTweak;
	public static boolean cakeTweak;
	public static boolean toolEffTweaks;
	public static boolean torchPlacer;
	public static boolean glitchingItemFix;
	// Item Configs
	public static boolean addFuels;
	// Misc Configs
	public static boolean giveGuideBook;
	public static boolean earlyGame;
	public static boolean rebirth;
	public static boolean horseArmor;
	public static boolean stackSizeTweaks;
	public static boolean lightning;
	public static boolean soundFixes;
	public static int foodToolTips;

	public static void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		Property property;

		// Mob Tweaks
		String category = "mob tweaks";
		List<String> propertyOrder = Lists.newArrayList();
		MobTweaks = config.getCategory(category);
		MobTweaks.setComment("Various Tweaks to Mobs");

		property = config.get(category, "Allow pet armor?", true).setRequiresMcRestart(true);
		property.comment = "Allows you to R-Click on TAMED pets with horse armor to armor them up!";
		petArmory = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Chickens Drop Extra Feathers", true).setRequiresMcRestart(true);
		property.comment = "If set to false, chicken drops will be unchanged";
		featherBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Cows Drop Extra Leather", true).setRequiresMcRestart(true);
		property.comment = "If set to false, cow drops will be unchanged";
		hideBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Skeletons Drop Extra Bones", true).setRequiresMcRestart(true);
		property.comment = "If set to false, skeleton drops will be unchanged";
		boneBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Squids Drop Extra Ink Sacs", true).setRequiresMcRestart(true);
		property.comment = "If set to false, squid drops will be unchanged";
		sacBuff = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Endermen Drop Extra Ender Pearls", true).setRequiresMcRestart(true);
		property.comment = "If set to false, enderman drops will be unchanged";
		enderpearlBuff = property.getBoolean();
		propertyOrder.add(property.getName());
		
		property = config.get(category, "Pluck Feathers from Chickens", true).setRequiresMcRestart(true);
		property.comment = "Allows chickens to have a feather plucked using shears";
		pluckFeather = property.getBoolean();
		propertyOrder.add(property.getName());
		
		property = config.get(category, "Disable Bats", true).setRequiresMcRestart(true);
		noBats = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Pig Zombies", true).setRequiresMcRestart(true);
		property.comment = "Also balances Ghast spawns";
		noPigZombies = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disallow Wither Spawning in the Overworld", false).setRequiresMcRestart(true);
		property.comment = "Special request - only allows the wither to be summoned in non-overworld dimensions";
		noOverworldWither = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Villager Sounds", false).setRequiresMcRestart(true);
		property.comment = "NOTE: CAUSES LOTS OF CONSOLE SPAM. NO KNOWN WAY AROUND THIS.";
		silenceVillagers = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Enabled", true).setRequiresMcRestart(true);
		property.comment = "Randomly spawns more difficult (but more lootworthy) enemies? Applies to ALL enemies";
		challengers = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs Rarity", 75, "There is a 1 in 'x' chance for Challenger mobs to spawn, this is 'x'", 1, Short.MAX_VALUE).setRequiresMcRestart(true);
		challengerMobRarity = property.getInt();
		propertyOrder.add(property.getName());

		property = config.get(category, "Challenger Mobs' Prefixes", challengerMobDefaults, "Renaming will not change anything, just their highlighted name").setRequiresMcRestart(false);
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

		property = config.get(category, "Enable Better Feather Falling", true).setRequiresMcRestart(true);
		property.comment = "Tweaks Feather Falling IV to negate ALL fall damage";
		featherFalling = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Item Disenchantment", true).setRequiresMcRestart(true);
		property.comment = "Allow crafting a piece of paper with an enchanted tool to disenchant said tool";
		disenchant = property.getBoolean();
		propertyOrder.add(property.getName());

		Enchantments.setPropertyOrder(propertyOrder);

		// Block Tweaks
		category = "block tweaks";
		propertyOrder = Lists.newArrayList();
		BlockTweaks = config.getCategory(category);
		BlockTweaks.setComment("Tweaks for Blocks");

		property = config.get(category, "Easy Crop Harvesting", true).setRequiresMcRestart(true);
		property.comment = "Allows for right-click-to-harvest on nearly any (including mod) crop. No seeds will be dropped - intended";
		cropHarvest = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Bonemeal Tweak", true).setRequiresMcRestart(true);
		property.comment = "Allows more things to be bonemealed; Nether Wart requires Blaze Powder";
		bonemealTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Cake Drops", true).setRequiresMcRestart(true);
		property.comment = "Uneaten Cakes can be broken and re-acquired";
		cakeTweak = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Efficiency Tweaks", true).setRequiresMcRestart(true);
		property.comment = "Fixes some tools NOT being effective on certain materials";
		toolEffTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Tool Torch Placement", true).setRequiresMcRestart(true);
		property.comment = "Right clicking with a tool will place a torch from your inventory";
		torchPlacer = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Glitching Item Fix", true).setRequiresMcRestart(true);
		property.comment = "Fixes common vanilla instances of items spawning and glitching everywhere by bypassing the spawning situation completely";
		glitchingItemFix = property.getBoolean();
		propertyOrder.add(property.getName());

		BlockTweaks.setPropertyOrder(propertyOrder);

		// Item Tweaks
		category = "item tweaks";
		propertyOrder = Lists.newArrayList();
		ItemTweaks = config.getCategory(category);
		ItemTweaks.setComment("Tweaks for Items");

		property = config.get(category, "Add Missing Items as Fuels", true).setRequiresMcRestart(true);
		property.comment = "Adds wooden items to fuel list if they were missing";
		addFuels = property.getBoolean();
		propertyOrder.add(property.getName());

		ItemTweaks.setPropertyOrder(propertyOrder);

		// Misc Features
		category = "miscellaneous";
		propertyOrder = Lists.newArrayList();
		MiscFeatures = config.getCategory(category);
		MiscFeatures.setComment("Other Tweaks");

		property = config.get(category, "Give Players V-Tweaks Guidebook?", true).setRequiresMcRestart(true);
		property.comment = "Allows you to prevent players from getting thet book if they won't need it / want it";
		giveGuideBook = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Change Base Game Mechanics?", true).setRequiresMcRestart(true);
		property.comment = "This config allows for flint and gravel to be a reasonably heavy part of crafting / early-game gameplay";
		earlyGame = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Ender Dragon Rebirth", true).setRequiresMcRestart(true);
		property.comment = "This features allows you to rebirth the ender dragon via a cryptic ritual..";
		rebirth = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Horse Armor Recipes", true).setRequiresMcRestart(true);
		property.comment = "Combining two pairs of undamaged leggings in an anvil will get you horse armor of that type";
		horseArmor = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Stack Size Tweaks", true).setRequiresMcRestart(true);
		property.comment = "Adjusts Max Stack Sizes of some vanilla items";
		stackSizeTweaks = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Disable Lightning?", true).setRequiresMcRestart(true);
		property.comment = "Disables lightning from spawning, it can get annoying";
		lightning = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Adjust Various Block Sounds?", true).setRequiresMcRestart(true);
		property.comment = "Small tweaks to fix inconsistencies";
		soundFixes = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Enable Food Value Tooltips?", 2, "0 disables the feature, 1 enables the features all the time, 2 enables the feature only while sneaking", 0, 2).setRequiresMcRestart(false);
		foodToolTips = property.getInt();
		propertyOrder.add(property.getName());

		MiscFeatures.setPropertyOrder(propertyOrder);

		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public void update(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equals(VTweaks.modid))
			loadConfiguration();
	}
}