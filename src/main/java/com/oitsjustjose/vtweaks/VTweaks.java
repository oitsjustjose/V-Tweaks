package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.event.DeathPoint;
import com.oitsjustjose.vtweaks.event.PingProtection;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.*;
import com.oitsjustjose.vtweaks.event.itemtweaks.*;
import com.oitsjustjose.vtweaks.event.mobtweaks.*;
import com.oitsjustjose.vtweaks.util.Config;
import com.oitsjustjose.vtweaks.util.ConfigParser;
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

@Mod(modid = VTweaks.MODID, name = VTweaks.NAME, version = VTweaks.VERSION, guiFactory = VTweaks.GUIFACTORY, updateJSON = VTweaks.UPDATER)
public class VTweaks
{
    public static final String MODID = "vtweaks";
    public static final String NAME = "V-Tweaks";
    public static final String VERSION = "@VERSION@";
    public static final String GUIFACTORY = "com.oitsjustjose.vtweaks.util.ConfigGUI$GUIFactory";
    public static final String UPDATER = "https://raw.githubusercontent.com/oitsjustjose/V-Tweaks/master/updater.json";


    @Instance(MODID)
    public static VTweaks instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new Config(event.getSuggestedConfigurationFile()));

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
        MinecraftForge.EVENT_BUS.register(new MobKiller());
        MinecraftForge.EVENT_BUS.register(new SheepDyeFix());

        // Enchantments
        new Enchantments();

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());
        MinecraftForge.EVENT_BUS.register(new LossPrevention());

        // Item Tweaks
        MinecraftForge.EVENT_BUS.register(new LeafEater());
        MinecraftForge.EVENT_BUS.register(new DropTweaks());
        MinecraftForge.EVENT_BUS.register(new GlitchingItemFix());
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
        if (Config.getInstance().enableExtraFuels)
        {
            MinecraftForge.EVENT_BUS.register(new WoodItemFuelHandler());
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);

        if (Config.getInstance().enableStackTweaks)
        {
            StackTweaks.registerTweak();
        }

        ConfigParser.parseItems();
        ConfigParser.parseBlocks();
    }
}