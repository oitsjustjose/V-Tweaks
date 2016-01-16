package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.enchantment.EnchantmentAutosmeltHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentHypermendingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentStepboostHandler;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.BlockEntityFix;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.event.blocktweaks.NetherWartTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.SignEditor;
import com.oitsjustjose.vtweaks.event.blocktweaks.SoundTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.StackTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.ToolEffTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.TorchHelper;
import com.oitsjustjose.vtweaks.event.mechanics.GamePlayHandler;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobs;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobsDrops;
import com.oitsjustjose.vtweaks.event.mobtweaks.DragonRebirth;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobDropBuffs;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobKiller;
import com.oitsjustjose.vtweaks.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.event.mobtweaks.VillagerTweak;
import com.oitsjustjose.vtweaks.util.CommonProxy;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.GuideBook;
import com.oitsjustjose.vtweaks.util.Recipes;

import net.minecraft.block.Block;
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

@Mod(modid = VTweaks.modid, name = VTweaks.name, version = VTweaks.version, guiFactory = VTweaks.guifactory, acceptedMinecraftVersions = "1.8, 1.8.8, 1.8.9")
public class VTweaks
{
	public static final String modid = "VTweaks";
	public static final String name = "V-Tweaks";
	public static final String version = "@VERSION@";
	public static final String guifactory = "com.oitsjustjose.vtweaks.util.ConfigGUI$GUIFactory";

	@Instance(modid)
	public static VTweaks instance;

	@SidedProxy(clientSide = "com.oitsjustjose.vtweaks.util.ClientProxy", serverSide = "com.oitsjustjose.vtweaks.util.CommonProxy", modId = modid)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new Config());

		Enchantments.initialize();

		if (Config.hypermendingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());

		if (Config.autosmeltID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		if (Config.stepboostID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());

		if (Config.lumberingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());

		if (Config.cropHarvest)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		if (Config.bonemealTweak)
		{
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
			MinecraftForge.EVENT_BUS.register(new NetherWartTweaks());
		}

		if (Config.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		if (Config.featherFalling)
			MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		if (Config.rebirth)
			MinecraftForge.EVENT_BUS.register(new DragonRebirth());

		if (Config.toolEffTweaks)
			MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());

		if (Config.torchPlacer)
			MinecraftForge.EVENT_BUS.register(new TorchHelper());

		if (Config.challengers)
		{
			MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
			MinecraftForge.EVENT_BUS.register(new ChallengerMobsDrops());
		}

		if (Config.earlyGame)
			GamePlayHandler.init();

		if (Config.lightning)
			MinecraftForge.EVENT_BUS.register(new StormTweak());

		if (Config.noPigZombies)
			Blocks.portal.setResistance(Float.MAX_VALUE);

		if (Config.signEditor)
			MinecraftForge.EVENT_BUS.register(new SignEditor());

		if (Config.silenceVillagers)
			MinecraftForge.EVENT_BUS.register(new VillagerTweak());

		if (Config.glitchingItemFix)
			MinecraftForge.EVENT_BUS.register(new BlockEntityFix());

		if (Config.giveGuideBook)
			MinecraftForge.EVENT_BUS.register(new GuideBook());

		if (Config.petArmory)
			MinecraftForge.EVENT_BUS.register(new PetArmory());

		MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
		MinecraftForge.EVENT_BUS.register(new MobKiller());
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new Recipes());

	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Recipes.registerRecipes();

		if (Config.soundFixes)
		{
			Blocks.anvil.setStepSound(Block.soundTypeMetal);
			Blocks.light_weighted_pressure_plate.setStepSound(Block.soundTypeMetal);
			Blocks.heavy_weighted_pressure_plate.setStepSound(Block.soundTypeMetal);
			MinecraftForge.EVENT_BUS.register(new SoundTweaks());
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
		if (Config.stackSizeTweaks)
			StackTweaks.registerTweaks();
	}
}