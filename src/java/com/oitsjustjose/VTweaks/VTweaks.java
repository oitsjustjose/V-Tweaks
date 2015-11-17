package com.oitsjustjose.VTweaks;

import com.oitsjustjose.VTweaks.Achievement.Achievements;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentAutosmeltHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentFeatherFallingHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentUnbreakableHandler;
import com.oitsjustjose.VTweaks.Enchantments.Enchantments;
import com.oitsjustjose.VTweaks.Events.ChickenFeatherBuff;
import com.oitsjustjose.VTweaks.Events.CowHideBuff;
import com.oitsjustjose.VTweaks.Events.CropHelper;
import com.oitsjustjose.VTweaks.Events.DragonRebirth;
import com.oitsjustjose.VTweaks.Events.SkeletonBoneBuff;
import com.oitsjustjose.VTweaks.Events.SquidSacBuff;
import com.oitsjustjose.VTweaks.Events.ToolTips;
import com.oitsjustjose.VTweaks.Proxy.Common;
import com.oitsjustjose.VTweaks.Tweaks.BlockTweaks;
import com.oitsjustjose.VTweaks.Tweaks.BonemealTweaks;
import com.oitsjustjose.VTweaks.Tweaks.CakeTweak;
import com.oitsjustjose.VTweaks.Tweaks.NetherWartTweaks;
import com.oitsjustjose.VTweaks.Tweaks.StackTweaks;
import com.oitsjustjose.VTweaks.Util.ConfigHandler;
import com.oitsjustjose.VTweaks.Util.RecipeList;

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
	public static final String version = "1.0";
	public static final String guifactory = "com.oitsjustjose.VTweaks.Util.Client.GUIFactory";

	@Instance(modid)
	public static VTweaks instance;

	@SidedProxy(clientSide = "com.oitsjustjose.VTweaks.Proxy.Client", serverSide = "com.oitsjustjose.VTweaks.Proxy.Common")
	public static Common proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// Initialize and Register my Configurable options
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigHandler());

		// Init Enchants
		Enchantments.initialize();

		// Registers Unbreakable if the ID is greater than zero
		if (ConfigHandler.unbreakableEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentUnbreakableHandler());

		// Registers Autosmelt if the ID is greater than zero
		if (ConfigHandler.autosmeltEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		// Initializes my mob drop buffs if they're enabled
		if (ConfigHandler.boneBuff)
			MinecraftForge.EVENT_BUS.register(new SkeletonBoneBuff());
		if (ConfigHandler.hideBuff)
			MinecraftForge.EVENT_BUS.register(new CowHideBuff());
		if (ConfigHandler.featherBuff)
			MinecraftForge.EVENT_BUS.register(new ChickenFeatherBuff());
		if (ConfigHandler.sacBuff)
			MinecraftForge.EVENT_BUS.register(new SquidSacBuff());

		// Initializes the crop feature if enabled
		if (ConfigHandler.cropFeature)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		// Initializes the bonemeal feature if enabled
		if (ConfigHandler.bonemealTweak)
		{
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
			MinecraftForge.EVENT_BUS.register(new NetherWartTweaks());
		}

		// Initializes the cake feature if enabled
		if (ConfigHandler.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		// Initializes better feather falling if enabled
		if (ConfigHandler.betterFeatherFalling)
			MinecraftForge.EVENT_BUS.register(new EnchantmentFeatherFallingHandler());

		// Initializes Dragon Rebirth if enabled
		if (ConfigHandler.rebirth)
			MinecraftForge.EVENT_BUS.register(new DragonRebirth());

		MinecraftForge.EVENT_BUS.register(new BlockTweaks());

		// Initializes other events.
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new RecipeList());
	
		// Achievement Things!
		Achievements.initialize();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		// Don't really have a lot of them yet:
		RecipeList.registerRecipes();

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
		StackTweaks.registerTweaks();
	}
}