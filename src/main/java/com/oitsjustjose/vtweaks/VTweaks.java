package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.enchantment.EnchantmentAutosmeltHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentHypermendingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentStepboostHandler;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.event.PingProtection;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.event.blocktweaks.StackTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.ToolEffTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.TorchHelper;
import com.oitsjustjose.vtweaks.event.itemtweaks.GamePlayHandler;
import com.oitsjustjose.vtweaks.event.itemtweaks.HangingItemFix;
import com.oitsjustjose.vtweaks.event.itemtweaks.WoodItemFuelHandler;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobs;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobsDrops;
import com.oitsjustjose.vtweaks.event.mobtweaks.FeatherPlucker;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobDropBuffs;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobKiller;
import com.oitsjustjose.vtweaks.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.event.mobtweaks.SheepDyeFix;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.ConfigItemParser;
import com.oitsjustjose.vtweaks.util.GuideNotifier;
import com.oitsjustjose.vtweaks.util.Recipes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = VTweaks.MODID, name = VTweaks.NAME, version = VTweaks.VERSION, guiFactory = VTweaks.GUIFACTORY, acceptedMinecraftVersions = "1.9.4", updateJSON = "https://raw.githubusercontent.com/oitsjustjose/V-Tweaks/master/updater.json")
public class VTweaks
{
	public static final String MODID = "VTweaks";
	public static final String NAME = "V-Tweaks";
	public static final String VERSION = "@VERSION@";
	public static final String GUIFACTORY = "com.oitsjustjose.vtweaks.util.ConfigGUI$GUIFactory";

	public static Config config;

	@Instance(MODID)
	public static VTweaks instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config = new Config(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(config);
		MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
		MinecraftForge.EVENT_BUS.register(new MobKiller());
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new Recipes());
		MinecraftForge.EVENT_BUS.register(new SheepDyeFix());
		MinecraftForge.EVENT_BUS.register(new GuideNotifier());
		Enchantments.initialize();

		if (config.hypermendingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());

		if (config.autosmeltID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		if (config.stepboostID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());

		if (config.lumberingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());

		if (config.cropHarvest)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		if (config.bonemealTweak)
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());

		if (config.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		if (config.featherFalling)
			MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		if (config.toolEffTweaks)
			MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());

		if (config.torchPlacer)
			MinecraftForge.EVENT_BUS.register(new TorchHelper());

		if (config.challengers)
		{
			MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
			MinecraftForge.EVENT_BUS.register(new ChallengerMobsDrops());
		}

		if (config.earlyGame)
			GamePlayHandler.init();

		if (config.lightning)
			MinecraftForge.EVENT_BUS.register(new StormTweak());

		if (config.noPigZombies)
			Blocks.PORTAL.setResistance(Float.MAX_VALUE);

		if (config.glitchingItemFix)
			MinecraftForge.EVENT_BUS.register(new HangingItemFix());

		if (config.petArmory)
			MinecraftForge.EVENT_BUS.register(new PetArmory());

		if (config.pluckFeather)
			MinecraftForge.EVENT_BUS.register(new FeatherPlucker());

		if (config.pingProtection)
			MinecraftForge.EVENT_BUS.register(new PingProtection());

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Recipes.registerRecipes();

		if (config.addFuels)
			GameRegistry.registerFuelHandler(new WoodItemFuelHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		if (config.stackSizeTweaks)
			StackTweaks.registerTweaks();
		ConfigItemParser.parseItems();
	}
}