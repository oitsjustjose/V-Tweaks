package com.oitsjustjose.VTweaks;

import com.oitsjustjose.VTweaks.Achievement.AchievementManager;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentAutosmeltHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentHypermendingHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentLumberingHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentStepboostHandler;
import com.oitsjustjose.VTweaks.Enchantments.Enchantments;
import com.oitsjustjose.VTweaks.Enchantments.FeatherFallingTweak;
import com.oitsjustjose.VTweaks.Events.ToolTips;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.BlockTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.BonemealTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.CakeTweak;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.CropHelper;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.NetherWartTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.StackTweaks;
import com.oitsjustjose.VTweaks.Events.MobTweaks.ChallengerMobs;
import com.oitsjustjose.VTweaks.Events.MobTweaks.ChallengerMobsDrops;
import com.oitsjustjose.VTweaks.Events.MobTweaks.ChickenFeatherBuff;
import com.oitsjustjose.VTweaks.Events.MobTweaks.CowHideBuff;
import com.oitsjustjose.VTweaks.Events.MobTweaks.DragonRebirth;
import com.oitsjustjose.VTweaks.Events.MobTweaks.MobKiller;
import com.oitsjustjose.VTweaks.Events.MobTweaks.SkeletonBoneBuff;
import com.oitsjustjose.VTweaks.Events.MobTweaks.SquidSacBuff;
import com.oitsjustjose.VTweaks.Proxy.Common;
import com.oitsjustjose.VTweaks.Util.Config;
import com.oitsjustjose.VTweaks.Util.Guide;
import com.oitsjustjose.VTweaks.Util.Recipes;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = VTweaks.modid, version = VTweaks.version, guiFactory = VTweaks.guifactory)
public class VTweaks
{
	public static final String modid = "VTweaks";
	public static final String version = "1.2";
	public static final String guifactory = "com.oitsjustjose.VTweaks.Util.Client.GUIFactory";

	@Instance(modid)
	public static VTweaks instance;

	@SidedProxy(clientSide = "com.oitsjustjose.VTweaks.Proxy.Client", serverSide = "com.oitsjustjose.VTweaks.Proxy.Common")
	public static Common proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// Initialize and Register my Configurable options
		Config.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new Config());

		// Init Enchants
		Enchantments.initialize();

		// Registers Hypermending if the ID is greater than zero
		if (Config.hypermendingEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());

		// Registers Autosmelt if the ID is greater than zero
		if (Config.autosmeltEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		// Registers Stepboost if the ID is greater than zero
		if (Config.stepboostEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());
		
		if (Config.lumberingEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());

		// Registers the event handler for getting the guidebook. You're getting it dammit
		MinecraftForge.EVENT_BUS.register(new Guide());

		// Initializes my mob drop buffs if they're enabled
		if (Config.boneBuff)
			MinecraftForge.EVENT_BUS.register(new SkeletonBoneBuff());
		if (Config.hideBuff)
			MinecraftForge.EVENT_BUS.register(new CowHideBuff());
		if (Config.featherBuff)
			MinecraftForge.EVENT_BUS.register(new ChickenFeatherBuff());
		if (Config.sacBuff)
			MinecraftForge.EVENT_BUS.register(new SquidSacBuff());

		// Initializes the crop feature if enabled
		if (Config.cropFeature)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		// Initializes the bonemeal feature if enabled
		if (Config.bonemealTweak)
		{
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
			MinecraftForge.EVENT_BUS.register(new NetherWartTweaks());
		}

		// Initializes the cake feature if enabled
		if (Config.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		// Initializes better feather falling if enabled
		if (Config.betterFeatherFalling)
			MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		// Initializes Dragon Rebirth if enabled
		if (Config.rebirth)
			MinecraftForge.EVENT_BUS.register(new DragonRebirth());

		// Initializes Block Tweaks if enabled
		if (Config.blockTweaks)
			MinecraftForge.EVENT_BUS.register(new BlockTweaks());

		// Initializes challenger mobs if enabled
		if (Config.challengers)
		{
			MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
			MinecraftForge.EVENT_BUS.register(new ChallengerMobsDrops());
		}

		// Initializes the MobKiller event handler regardless, the configs
		// are checked inside of that class itself.
		MinecraftForge.EVENT_BUS.register(new MobKiller());

		// Initializes other events.
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new Recipes());

		// Achievement Things!
		AchievementManager.initialize();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Don't really have a lot of them yet:
		Recipes.registerRecipes();

		// Run Renderers for mobs
		proxy.registerRenderers();
		// Run Renderers for items..
		proxy.registerItemRenderers();
		// Doesn't actually do anything for now
		proxy.registerAudio();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// All of this is in postInit so that it is **ideally** done after other
		// mods that may do the same thing, like TiCon
		// Adds these vanilla blocks to creative tabs. Why weren't they already
		// there?!
		Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);

		// Registers Stack Tweaks, if enabled
		if (Config.stackSizeTweaks)
			StackTweaks.registerTweaks();

		if (true)
		{
			Blocks.portal.setResistance(Float.MAX_VALUE);
		}
	}
}