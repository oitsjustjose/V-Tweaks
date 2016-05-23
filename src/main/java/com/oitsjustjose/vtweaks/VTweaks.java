package com.oitsjustjose.vtweaks;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.enchantment.EnchantmentAutosmeltHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentHypermendingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentStepboostHandler;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweakNetherwart;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.event.blocktweaks.SoundTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.StackTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.ToolEffTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.TorchHelper;
import com.oitsjustjose.vtweaks.event.itemtweaks.GamePlayHandler;
import com.oitsjustjose.vtweaks.event.itemtweaks.HangingItemFix;
import com.oitsjustjose.vtweaks.event.itemtweaks.WoodItemFuelHandler;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobs;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobsDrops;
import com.oitsjustjose.vtweaks.event.mobtweaks.DragonRebirth;
import com.oitsjustjose.vtweaks.event.mobtweaks.FeatherPlucker;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobDropBuffs;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobKiller;
import com.oitsjustjose.vtweaks.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.event.mobtweaks.SheepDyeFix;
import com.oitsjustjose.vtweaks.util.BookItems;
import com.oitsjustjose.vtweaks.util.CommonProxy;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.ConfigItemParser;
import com.oitsjustjose.vtweaks.util.Recipes;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = VTweaks.MODID, name = VTweaks.NAME, version = VTweaks.VERSION, guiFactory = VTweaks.GUIFACTORY, acceptedMinecraftVersions = "1.8, 1.8.8, 1.8.9")
public class VTweaks
{
	public static final String MODID = "VTweaks";
	public static final String NAME = "V-Tweaks";
	public static final String VERSION = "@VERSION@";
	public static final String GUIFACTORY = "com.oitsjustjose.vtweaks.util.ConfigGUI$GUIFactory";

	public static ArrayList<ItemStack> challengerLootTable;
	public static Config modConfig;

	@Instance(MODID)
	public static VTweaks instance;

	@SidedProxy(clientSide = "com.oitsjustjose.vtweaks.util.ClientProxy", serverSide = "com.oitsjustjose.vtweaks.util.CommonProxy", modId = MODID)
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		modConfig = new Config(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(modConfig);
		MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
		MinecraftForge.EVENT_BUS.register(new MobKiller());
		MinecraftForge.EVENT_BUS.register(new ToolTips());
		MinecraftForge.EVENT_BUS.register(new Recipes());
		MinecraftForge.EVENT_BUS.register(new SheepDyeFix());

		Enchantments.initialize();

		if (modConfig.hypermendingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());

		if (modConfig.autosmeltID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());

		if (modConfig.stepboostID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());

		if (modConfig.lumberingID > 0)
			MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());

		if (modConfig.cropHarvest)
			MinecraftForge.EVENT_BUS.register(new CropHelper());

		if (modConfig.bonemealTweak)
		{
			MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
			MinecraftForge.EVENT_BUS.register(new BonemealTweakNetherwart());
		}

		if (modConfig.cakeTweak)
			MinecraftForge.EVENT_BUS.register(new CakeTweak());

		if (modConfig.featherFalling)
			MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());

		if (modConfig.rebirth)
			MinecraftForge.EVENT_BUS.register(new DragonRebirth());

		if (modConfig.toolEffTweaks)
			MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());

		if (modConfig.torchPlacer)
			MinecraftForge.EVENT_BUS.register(new TorchHelper());

		if (modConfig.challengers)
		{
			MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
			MinecraftForge.EVENT_BUS.register(new ChallengerMobsDrops());
		}

		if (modConfig.earlyGame)
			GamePlayHandler.init();

		if (modConfig.lightning)
			MinecraftForge.EVENT_BUS.register(new StormTweak());

		if (modConfig.noPigZombies)
			Blocks.portal.setResistance(Float.MAX_VALUE);

		if (modConfig.silenceVillagers)
			MinecraftForge.EVENT_BUS.register(SoundTweaks.VillagerTweak.getInstance());

		if (modConfig.glitchingItemFix)
			MinecraftForge.EVENT_BUS.register(new HangingItemFix());

		if (modConfig.giveGuideBook)
			MinecraftForge.EVENT_BUS.register(new BookItems());

		if (modConfig.petArmory)
			MinecraftForge.EVENT_BUS.register(new PetArmory());

		if (modConfig.pluckFeather)
			MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Recipes.registerRecipes();

		if (modConfig.soundFixes)
		{
			Blocks.anvil.setStepSound(Block.soundTypeMetal);
			Blocks.light_weighted_pressure_plate.setStepSound(Block.soundTypeMetal);
			Blocks.heavy_weighted_pressure_plate.setStepSound(Block.soundTypeMetal);
			MinecraftForge.EVENT_BUS.register(new SoundTweaks());
		}

		if (modConfig.addFuels)
			GameRegistry.registerFuelHandler(new WoodItemFuelHandler());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Blocks.command_block.setCreativeTab(CreativeTabs.tabRedstone);
		if (modConfig.stackSizeTweaks)
			StackTweaks.registerTweaks();
		challengerLootTable = ConfigItemParser.getChallengerLootTable();
	}
}