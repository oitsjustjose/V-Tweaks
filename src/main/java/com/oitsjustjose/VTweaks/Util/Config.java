package com.oitsjustjose.VTweaks.Util;

import java.io.File;

import com.oitsjustjose.VTweaks.VTweaks;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	public static Configuration config;

	// Buff Configs
	public static boolean featherBuff;
	public static boolean hideBuff;
	public static boolean boneBuff;
	public static boolean sacBuff;
	// Enchantment Configs
	public static int hypermendingEnchantmentID;
	public static int autosmeltEnchantmentID;
	public static int stepboostEnchantmentID;
	public static boolean betterFeatherFalling;
	// Misc Configs
	public static boolean cropFeature;
	public static boolean bonemealTweak;
	public static boolean cakeTweak;
	public static boolean rebirth;
	public static boolean horseArmor;
	public static boolean stackSizeTweaks;
	public static boolean disenchant;
	public static boolean blockTweaks;
	// Mob Configs
	public static boolean challengers;
	public static String[] challengerMobDefaults = new String[] { "Tanky", "Hungry", "Ranger", "Mage", "Pyro",
			"Zestonian", "Resilient", "Hyper" };
	public static String[] challengerMobs;
	public static int challengerMobRarity;
	public static boolean noBats;
	public static boolean noPigZombies;

	public static void init(File configFile)
	{
		// Create the configuration object from the given configuration file
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		hypermendingEnchantmentID = config.getInt("#Hypermending Enchantment ID", config.CATEGORY_GENERAL, 233, 0, 255,
				"The Enchantment ID for VTweaks' Hypermending Enchantment. If set to 0, the enchantment is disabled");

		autosmeltEnchantmentID = config.getInt("#Autosmelt Enchantment ID", config.CATEGORY_GENERAL, 234, 0, 255,
				"The Enchantment ID for VTweaks' Autosmelt Enchantment. If set to 0, the enchantment is disabled");

		stepboostEnchantmentID = config.getInt("#Step Boost Enchantment ID", config.CATEGORY_GENERAL, 235, 0, 255,
				"The Enchantment ID for VTweaks' Step Boost Enchantment. If set to 0, the enchantment is disabled");

		featherBuff = config.getBoolean("Chickens Drop Extra Feathers", config.CATEGORY_GENERAL, true,
				"If set to false, chicken drops will be unchanged");

		hideBuff = config.getBoolean("Cows Drop Extra Leather", config.CATEGORY_GENERAL, true,
				"If set to false, cow drops will be unchanged");

		boneBuff = config.getBoolean("Skeletons Drop Extra Bones and Bonemeal", config.CATEGORY_GENERAL, true,
				"If set to false, skeleton drops will be unchanged");

		sacBuff = config.getBoolean("Squids Drop Extra Ink Sacs", config.CATEGORY_GENERAL, true,
				"If set to false, squid drops will be unchanged");

		cropFeature = config.getBoolean("Allow Right-Click-To-Harvest Feature on Crops", config.CATEGORY_GENERAL, true,
				"This feature attempts to allow right-clicking on fully grown crops (on any block / mod block extending BlockCrop) that is fully grown"
						+ "\n" + "\n"
						+ "You will not get seeds back when harvesting like this, but you will get an extra bonus as a trade-off");

		betterFeatherFalling = config.getBoolean("Enable Better Feather Falling", config.CATEGORY_GENERAL, true,
				"This feature causes 100% negation of any fall damage at all if your boots' Feather Falling level is IV or higher");

		rebirth = config.getBoolean("Enable Ender Dragon Rebirth Feature", config.CATEGORY_GENERAL, true,
				"This features allows you to rebirth the ender dragon via a cryptic ritual..");

		horseArmor = config.getBoolean("Enable Horse Armor Recipes", config.CATEGORY_GENERAL, true,
				"Combining two pairs of undamaged leggings (of the right kind) in an anvil will get you horse armor of that type"
						+ "\n" + "\n" + "Set this option to false to disable this feature");

		stackSizeTweaks = config.getBoolean("Enable Vanilla Item Stack Size Tweaks", config.CATEGORY_GENERAL, true,
				"If set to false, all items' stack sizes will remain unchanged");

		disenchant = config.getBoolean("Enable Disenchantment Recipes", config.CATEGORY_GENERAL, true,
				"Allow crafting a piece of paper with an enchanted tool to disenchant said tool");

		bonemealTweak = config.getBoolean("Enable Bonemeal Tweak", config.CATEGORY_GENERAL, true,
				"Enable Cactus and Sugar Cane to be bonemealed, and NetherWart to be blaze-powdered");

		cakeTweak = config.getBoolean("Enable Cake Tweak", config.CATEGORY_GENERAL, true,
				"If set to false, cake will not be dropped from an uneaten cake, as per vanilla mechanics");

		blockTweaks = config.getBoolean("Enable Block Efficiency Tweaks", config.CATEGORY_GENERAL, true,
				"Enable fixes to tool efficiencies on certain blocks");

		challengers = config.getBoolean("Challenger Mobs Enabled", config.CATEGORY_GENERAL, true,
				"Enable the spawning of randomly more difficult (but more lootworthy) enemies? Applies to ALL enemies");

		challengerMobs = config.get(config.CATEGORY_GENERAL, "Challenger Mobs' Names", challengerMobDefaults,
				"Names for the Challenger Mobs. Renaming will not effect their bonuses, just their highlighted name")
				.getStringList();

		challengerMobRarity = config.getInt("Challenger Mob Rarity", config.CATEGORY_GENERAL, 75, 1, Short.MAX_VALUE,
				"There is a 1 in x chance of a mob spawning as a Challenger Mob. This number is 'x'");

		noBats = config.getBoolean("Disable Bats", config.CATEGORY_GENERAL, true,
				"Hate bats? Leave this as 'true' to disable their spawn");

		noPigZombies = config.getBoolean("Disable Pig Zombies", config.CATEGORY_GENERAL, true,
				"Do Pig Zombies really have a purpose? Leave this as 'true' to disable their spawn");

		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equalsIgnoreCase(VTweaks.modid))
			loadConfiguration();
	}
}