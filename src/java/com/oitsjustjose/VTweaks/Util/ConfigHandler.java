package com.oitsjustjose.VTweaks.Util;

import java.io.File;

import com.oitsjustjose.VTweaks.VTweaks;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler
{
	public static Configuration configuration;

	// Buff Configs
	public static boolean featherBuff;
	public static boolean hideBuff;
	public static boolean boneBuff;
	public static boolean sacBuff;
	// Enchantment Configs
	public static int unbreakableEnchantmentID;
	public static int autosmeltEnchantmentID;
	public static boolean betterFeatherFalling;
	// Misc Configs
	public static boolean cropFeature;
	public static boolean bonemealTweak;
	public static boolean cakeTweak;
	public static boolean rebirth;
	public static boolean horseArmor;
	public static boolean stackSizeTweaks;
	public static boolean disenchant;

	public static void init(File configFile)
	{
		// Create the configuration object from the given configuration file
		if (configuration == null)
		{
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		unbreakableEnchantmentID = configuration.getInt("Unbreakable Enchantment ID", Configuration.CATEGORY_GENERAL,
				233, 0, 255,
				"The Enchantment ID for VTweaks' Unbreakable Enchantment. If set to 0, the enchantment is disabled.");

		autosmeltEnchantmentID = configuration.getInt("Autosmelt Enchantment ID", Configuration.CATEGORY_GENERAL, 234,
				0, 255,
				"The Enchantment ID for VTweaks' Autosmelt Enchantment. If set to 0, the enchantment is disabled.");

		featherBuff = configuration.getBoolean("Chickens Drop Extra Feathers?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, chicken drops will be unchanged.");

		hideBuff = configuration.getBoolean("Cows Drop Extra Leather?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, cow drops will be unchanged.");

		boneBuff = configuration.getBoolean("Skeletons Drop Extra Bones and Bonemeal?", Configuration.CATEGORY_GENERAL,
				true, "If set to false, skeleton drops will be unchanged.");

		sacBuff = configuration.getBoolean("Squids Drop Extra Ink Sacs?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, squid drops will be unchanged.");

		cropFeature = configuration.getBoolean("Allow Right-Click-To-Harvest Feature on Crops?",
				Configuration.CATEGORY_GENERAL, true,
				"This feature attempts to allow right-clicking on fully grown crops (on any block / mob block extending BlockCrop) that is fully grown."
						+ " You will not get seeds back when harvesting like this, but you will get an extra bonus as a trade-off");

		betterFeatherFalling = configuration.getBoolean("Enable Better Feather Falling?",
				Configuration.CATEGORY_GENERAL, true,
				"This feature causes 100% negation of any fall damage at all if your boots' Feather Falling level is IV or higher");

		rebirth = configuration.getBoolean("Enable Ender Dragon Rebirth Feature?", Configuration.CATEGORY_GENERAL, true,
				"This features allows you to rebirth the ender dragon via a cryptic ritual...");

		horseArmor = configuration.getBoolean("Enable Horse Armor Recipes?", Configuration.CATEGORY_GENERAL, true,
				"Combining two pairs of undamaged leggings (of the right kind) in an anvil will get you horse armor of that type."
						+ "Set this option to false to disable this feature.");

		stackSizeTweaks = configuration.getBoolean("Enable Vanilla Item Stack Size Tweaks?",
				Configuration.CATEGORY_GENERAL, true, "If set to false, all items' stack sizes will remain unchanged");

		disenchant = configuration.getBoolean("Enable Disenchantment Recipes?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, you won't be able to combine an enchanted tool with a piece of paper to disenchant it");

		bonemealTweak = configuration.getBoolean("Enable Bonemeal Tweak?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, Cactus and Sugar Cane will still be unable to be bonemealed");

		cakeTweak = configuration.getBoolean("Enable Cake Tweak?", Configuration.CATEGORY_GENERAL, true,
				"If set to false, cake will not be dropped from an uneaten cake, as per vanilla mechanics");

		if (configuration.hasChanged())
			configuration.save();
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equalsIgnoreCase(VTweaks.modid))
			loadConfiguration();
	}
}