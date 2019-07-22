package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.event.DeathPoint;
import com.oitsjustjose.vtweaks.event.StormTweak;
import com.oitsjustjose.vtweaks.event.ToolTips;
import com.oitsjustjose.vtweaks.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.event.blocktweaks.ToolEffTweaks;
import com.oitsjustjose.vtweaks.event.itemtweaks.ConcreteTweaks;
import com.oitsjustjose.vtweaks.event.itemtweaks.DropTweaks;
import com.oitsjustjose.vtweaks.event.mobtweaks.ChallengerMobs;
import com.oitsjustjose.vtweaks.event.mobtweaks.FeatherPlucker;
import com.oitsjustjose.vtweaks.event.mobtweaks.MobDropBuffs;
import com.oitsjustjose.vtweaks.event.mobtweaks.PeacefulSurface;
import com.oitsjustjose.vtweaks.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.event.mobtweaks.SheepDyeFix;
import com.oitsjustjose.vtweaks.util.ConfigParser;
import com.oitsjustjose.vtweaks.util.GuideNotifier;
import com.oitsjustjose.vtweaks.util.ModConfig;
import com.oitsjustjose.vtweaks.util.Recipes;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = VTweaks.MODID, name = VTweaks.NAME, version = VTweaks.VERSION, updateJSON = VTweaks.UPDATER)
public class VTweaks
{
    public static final String MODID = "vtweaks";
    public static final String NAME = "V-Tweaks";
    public static final String VERSION = "@VERSION@";
    public static final String UPDATER = "https://raw.githubusercontent.com/oitsjustjose/V-Tweaks/1.12/updater.json";

    @Instance(MODID)
    public static VTweaks instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new ModConfig.EventHandler());

        // Enchantments
        MinecraftForge.EVENT_BUS.register(Enchantments.getInstance());

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new MobDropBuffs());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
        MinecraftForge.EVENT_BUS.register(new SheepDyeFix());
        MinecraftForge.EVENT_BUS.register(new PeacefulSurface());

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());

        // Item Tweaks
        MinecraftForge.EVENT_BUS.register(new DropTweaks());
        MinecraftForge.EVENT_BUS.register(new ConcreteTweaks());

        // Miscellaneous Features
        MinecraftForge.EVENT_BUS.register(new ToolTips());
        MinecraftForge.EVENT_BUS.register(new StormTweak());
        MinecraftForge.EVENT_BUS.register(new DeathPoint());

        // Default Features
        MinecraftForge.EVENT_BUS.register(new GuideNotifier());
        MinecraftForge.EVENT_BUS.register(new Recipes());

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.CONCRETE_POWDER),
                ConcreteTweaks.CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        Blocks.COMMAND_BLOCK.setCreativeTab(CreativeTabs.REDSTONE);
        ConfigParser.parseItems();
    }
}