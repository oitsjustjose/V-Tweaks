package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.enchantment.EnchantmentAutosmeltHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentHypermendingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentStepboostHandler;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.event.DeathPoint;
import com.oitsjustjose.vtweaks.event.PingProtection;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.event.blocktweaks.LavaLossPrevention;
import com.oitsjustjose.vtweaks.event.blocktweaks.StackTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.ToolEffTweaks;
import com.oitsjustjose.vtweaks.event.itemtweaks.DropTweaks;
import com.oitsjustjose.vtweaks.event.itemtweaks.GamePlayHandler;
import com.oitsjustjose.vtweaks.event.itemtweaks.HangingItemFix;
import com.oitsjustjose.vtweaks.event.itemtweaks.TorchHelper;
import com.oitsjustjose.vtweaks.event.itemtweaks.WoodItemFuelHandler;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobs;
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

@Mod(modid = VTweaks.MODID, name = VTweaks.NAME, version = VTweaks.VERSION, guiFactory = VTweaks.GUIFACTORY, acceptedMinecraftVersions = "1.11", updateJSON = "https://raw.githubusercontent.com/oitsjustjose/V-Tweaks/master/updater.json")
public class VTweaks
{
	public static final String MODID = "vtweaks";
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

		GamePlayHandler.initialize();
		MinecraftForge.EVENT_BUS.register(config);

		// Mob Tweaks
		MinecraftForge.EVENT_BUS.register(new PetArmory());
		MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
		MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
		MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
		MinecraftForge.EVENT_BUS.register(new MobKiller());
		MinecraftForge.EVENT_BUS.register(new SheepDyeFix());

		// Enchantments
		Enchantments.initialize();
		MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());
		MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());
		MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());
		MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
		MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		// Block Tweaks
		MinecraftForge.EVENT_BUS.register(new CropHelper());
		MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
		MinecraftForge.EVENT_BUS.register(new CakeTweak());
		MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());
		MinecraftForge.EVENT_BUS.register(new LavaLossPrevention());

		// Item Tweaks
		MinecraftForge.EVENT_BUS.register(new DropTweaks());
		MinecraftForge.EVENT_BUS.register(new HangingItemFix());
		MinecraftForge.EVENT_BUS.register(new TorchHelper());

		// Miscellaneous Features
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new StormTweak());
		MinecraftForge.EVENT_BUS.register(new PingProtection());
		MinecraftForge.EVENT_BUS.register(new DeathPoint());

		// Default Features
		MinecraftForge.EVENT_BUS.register(new GuideNotifier());
		MinecraftForge.EVENT_BUS.register(new Recipes());

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if (config.enableWoodItemFuelHandler)
			GameRegistry.registerFuelHandler(new WoodItemFuelHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
		
		if (config.enableStackTweaks)
			StackTweaks.registerTweak();
		
		ConfigItemParser.parseItems();	
	}
}