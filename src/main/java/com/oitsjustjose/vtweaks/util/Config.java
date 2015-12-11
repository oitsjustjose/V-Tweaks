package com.oitsjustjose.vtweaks.util;

import java.io.File;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	public static int lumberingEnchantmentID;
	public static boolean betterFeatherFalling;
	// Tweak Configs
	public static boolean cropFeature;
	public static boolean bonemealTweak;
	public static boolean cakeTweak;
	public static boolean rebirth;
	public static boolean horseArmor;
	public static boolean stackSizeTweaks;
	public static boolean disenchant;
	public static boolean blockTweaks;
	public static boolean torchHelper;
	public static int foodToolTips;
	// Mob Configs
	public static boolean challengers;
	public static String[] challengerMobDefaults = new String[] { "Tanky", "Hungry", "Ranger", "Mage", "Pyro", "Zestonian", "Resilient", "Hyper" };
	public static String[] challengerMobs;
	public static int challengerMobRarity;
	public static boolean noBats;
	public static boolean noPigZombies;
	//Gameplay tweaks
	public static boolean changeEarlyGame;

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
		// Buff Configs
		featherBuff = config.getBoolean("Chickens Drop Extra Feathers", config.CATEGORY_GENERAL, true, "If set to false, chicken drops will be unchanged");

		hideBuff = config.getBoolean("Cows Drop Extra Leather", config.CATEGORY_GENERAL, true, "If set to false, cow drops will be unchanged");

		boneBuff = config.getBoolean("Skeletons Drop Extra Bones", config.CATEGORY_GENERAL, true, "If set to false, skeleton drops will be unchanged");

		sacBuff = config.getBoolean("Squids Drop Extra Ink Sacs", config.CATEGORY_GENERAL, true, "If set to false, squid drops will be unchanged");

		// Enchantment Configs
		hypermendingEnchantmentID = config.getInt("#Hypermending Enchantment ID", config.CATEGORY_GENERAL, 233, 0, 255,
				"The Enchantment ID for vtweaks' Hypermending Enchantment. If set to 0, the enchantment is disabled");

		autosmeltEnchantmentID = config.getInt("#Autosmelt Enchantment ID", config.CATEGORY_GENERAL, 234, 0, 255,
				"The Enchantment ID for vtweaks' Autosmelt Enchantment. If set to 0, the enchantment is disabled");

		stepboostEnchantmentID = config.getInt("#Step Boost Enchantment ID", config.CATEGORY_GENERAL, 235, 0, 255,
				"The Enchantment ID for vtweaks' Step Boost Enchantment. If set to 0, the enchantment is disabled");

		lumberingEnchantmentID = config.getInt("#Lumbering Enchantment ID", config.CATEGORY_GENERAL, 236, 0, 255,
				"The Enchantment ID for vtweaks' Lumbering Enchantment. If set to 0, the enchantment is disabled");

		// Tweak Configs
		cropFeature = config.getBoolean("Allow Right-Click-To-Harvest Feature on Crops", config.CATEGORY_GENERAL, true,
				"This feature attempts to allow right-clicking on fully grown crops (on any block / mod block extending BlockCrop) that is fully grown" + "\n" + "\n"
						+ "You will not get seeds back when harvesting like this, but you will get an extra bonus as a trade-off");

		betterFeatherFalling = config.getBoolean("Enable Better Feather Falling", config.CATEGORY_GENERAL, true,
				"This feature causes 100% negation of any fall damage at all if your boots' Feather Falling level is IV or higher");

		rebirth = config.getBoolean("Enable Ender Dragon Rebirth Feature", config.CATEGORY_GENERAL, true, "This features allows you to rebirth the ender dragon via a cryptic ritual..");

		horseArmor = config.getBoolean("Enable Horse Armor Recipes", config.CATEGORY_GENERAL, true,
				"Combining two pairs of undamaged leggings (of the right kind) in an anvil will get you horse armor of that type" + "\n" + "\n" + "Set this option to false to disable this feature");

		stackSizeTweaks = config.getBoolean("Enable Vanilla Item Stack Size Tweaks", config.CATEGORY_GENERAL, true, "If set to false, all items' stack sizes will remain unchanged");

		disenchant = config.getBoolean("Enable Disenchantment Recipes", config.CATEGORY_GENERAL, true, "Allow crafting a piece of paper with an enchanted tool to disenchant said tool");

		bonemealTweak = config.getBoolean("Enable Bonemeal Tweak", config.CATEGORY_GENERAL, true, "Enable Cactus and Sugar Cane to be bonemealed, and NetherWart to be blaze-powdered");

		cakeTweak = config.getBoolean("Enable Cake Tweak", config.CATEGORY_GENERAL, true, "If set to false, cake will not be dropped from an uneaten cake, as per vanilla mechanics");

		blockTweaks = config.getBoolean("Enable Block Efficiency Tweaks", config.CATEGORY_GENERAL, true, "Enable fixes to tool efficiencies on certain blocks");

		torchHelper = config.getBoolean("Enable Torch Helper Ability?", config.CATEGORY_GENERAL, true,
				"Enables a neat feature to right click with a tool item to place a torch (if torches are anywhere in your inventory)");

		foodToolTips = config.getInt("Enable Food Value Tooltips?", config.CATEGORY_GENERAL, 1, 0, 2,
				"0 disables the feature, 1 enables the features all the time, 2 enables the feature only while sneaking");

		// Mob Configs
		challengers = config.getBoolean("Challenger Mobs Enabled", config.CATEGORY_GENERAL, true,
				"Enable the spawning of randomly more difficult (but more lootworthy) enemies? Applies to ALL enemies");

		challengerMobs = config.get(config.CATEGORY_GENERAL, "Challenger Mobs' Names", challengerMobDefaults,
				"Names for the Challenger Mobs. Renaming will not effect their bonuses, just their highlighted name").getStringList();

		challengerMobRarity = config.getInt("Challenger Mob Rarity", config.CATEGORY_GENERAL, 75, 1, Short.MAX_VALUE,
				"There is a 1 in x chance of a mob spawning as a Challenger Mob. This number is 'x'");

		noBats = config.getBoolean("Disable Bats", config.CATEGORY_GENERAL, true, "Hate bats? Leave this as 'true' to disable their spawn");

		noPigZombies = config.getBoolean("Disable Pig Zombies", config.CATEGORY_GENERAL, true, "Do Pig Zombies really have a purpose? Leave this as 'true' to disable their spawn");
		
		//Gameplay
		changeEarlyGame = config.getBoolean("Change Early Game Mechanics?", config.CATEGORY_GENERAL, true, "This config allows for flint and gravel to be a reasonably heavy part of crafting / early-game gameplay");

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