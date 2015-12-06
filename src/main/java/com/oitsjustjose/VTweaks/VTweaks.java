package com.oitsjustjose.VTweaks;

import com.oitsjustjose.VTweaks.Achievement.AchievementManager;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentAutosmeltHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentHypermendingHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentLumberingHandler;
import com.oitsjustjose.VTweaks.Enchantments.EnchantmentStepboostHandler;
import com.oitsjustjose.VTweaks.Enchantments.Enchantments;
import com.oitsjustjose.VTweaks.Enchantments.FeatherFallingTweak;
import com.oitsjustjose.VTweaks.Events.EasyGUI;
import com.oitsjustjose.VTweaks.Events.ToolTips;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.BlockTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.BonemealTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.CakeTweak;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.CropHelper;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.NetherWartTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.StackTweaks;
import com.oitsjustjose.VTweaks.Events.BlockTweaks.TorchHelper;
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
import com.oitsjustjose.VTweaks.Util.GuideBook;
import com.oitsjustjose.VTweaks.Util.KeyBindings;
import com.oitsjustjose.VTweaks.Util.Recipes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = VTweaks.modid, name = VTweaks.name, version = VTweaks.version, guiFactory = VTweaks.guifactory)
public class VTweaks
{
	public static final String modid = "VTweaks";
	public static final String name = "V-Tweaks";
	public static final String version = "1.2";
	public static final String guifactory = "com.oitsjustjose.VTweaks.Util.Client.GUIFactory";

	@Instance(modid)
	public static VTweaks instance;

	@SidedProxy(clientSide = "com.oitsjustjose.VTweaks.Proxy.Client", serverSide = "com.oitsjustjose.VTweaks.Proxy.Common")
	public static Common proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new Config());

		AchievementManager.initialize();
		Enchantments.initialize();
		KeyBindings.initialize();

		if (Config.hypermendingEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());

		if (Config.autosmeltEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		if (Config.stepboostEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());

		if (Config.lumberingEnchantmentID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());

		if (Config.boneBuff)
			MinecraftForge.EVENT_BUS.register(new SkeletonBoneBuff());

		if (Config.hideBuff)
			MinecraftForge.EVENT_BUS.register(new CowHideBuff());

		if (Config.featherBuff)
			MinecraftForge.EVENT_BUS.register(new ChickenFeatherBuff());

		if (Config.sacBuff)
			MinecraftForge.EVENT_BUS.register(new SquidSacBuff());

		if (Config.cropFeature)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		if (Config.bonemealTweak)
		{
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
			MinecraftForge.EVENT_BUS.register(new NetherWartTweaks());
		}

		if (Config.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		if (Config.betterFeatherFalling)
			MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		if (Config.rebirth)
			MinecraftForge.EVENT_BUS.register(new DragonRebirth());

		if (Config.blockTweaks)
			MinecraftForge.EVENT_BUS.register(new BlockTweaks());

		if (Config.torchHelper)
			MinecraftForge.EVENT_BUS.register(new TorchHelper());

		if (Config.challengers)
		{
			MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
			MinecraftForge.EVENT_BUS.register(new ChallengerMobsDrops());
		}

		MinecraftForge.EVENT_BUS.register(new MobKiller());
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new Recipes());
		MinecraftForge.EVENT_BUS.register(new EasyGUI());
		MinecraftForge.EVENT_BUS.register(new GuideBook());

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Recipes.registerRecipes();

		proxy.registerRenderers();
		proxy.registerItemRenderers();
		proxy.registerAudio();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
		Blocks.portal.setResistance(Float.MAX_VALUE);

		if (Config.stackSizeTweaks)
			StackTweaks.registerTweaks();
	}
}