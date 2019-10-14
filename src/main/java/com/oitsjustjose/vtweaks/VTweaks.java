package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.config.CommonConfig;
import com.oitsjustjose.vtweaks.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentImperishable;
import com.oitsjustjose.vtweaks.enchantment.EnchantmentLumbering;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentImperishableHandler;
import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentLumberingHandler;
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
import com.oitsjustjose.vtweaks.event.mobtweaks.PeacefulSurface;
import com.oitsjustjose.vtweaks.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.util.Constants;
import com.oitsjustjose.vtweaks.util.GuideNotifier;
import com.oitsjustjose.vtweaks.util.Recipes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.DispenserBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class VTweaks
{
    private static VTweaks instance;
    public Logger LOGGER = LogManager.getLogger();

    public static Enchantment lumbering = new EnchantmentLumbering();
    public static Enchantment imperishable = new EnchantmentImperishable();

    public VTweaks()
    {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        this.configSetup();
    }

    public static VTweaks getInstance()
    {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent event)
    {
        // Enchantments
        MinecraftForge.EVENT_BUS.register(new Enchantments());
        MinecraftForge.EVENT_BUS.register(new Recipes());

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
        MinecraftForge.EVENT_BUS.register(new PeacefulSurface());

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ToolEffTweaks());

        // Item Tweaks
        MinecraftForge.EVENT_BUS.register(new DropTweaks());
        MinecraftForge.EVENT_BUS.register(new ConcreteTweaks());
        ConcreteTweaks.powderBlocks.forEach((item) -> {
            DispenserBlock.registerDispenseBehavior(item, ConcreteTweaks.CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM);
        });

        // Miscellaneous Features
        MinecraftForge.EVENT_BUS.register(new ToolTips());
        MinecraftForge.EVENT_BUS.register(new StormTweak());
        MinecraftForge.EVENT_BUS.register(new DeathPoint());

        // Default Features
        MinecraftForge.EVENT_BUS.register(new GuideNotifier());
    }

    private void configSetup()
    {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-common.toml"));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void registerEnchantments(final RegistryEvent.Register<Enchantment> enchantmentRegistryEvent)
        {

            if (EnchantmentConfig.ENABLE_LUMBERING.get())
            {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.lumbering);
                MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
            }

            if (EnchantmentConfig.ENABLE_IMPERISHABLE.get())
            {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.imperishable);
                MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
            }

            if (EnchantmentConfig.ENABLE_FF_TWEAK.get())
            {
                MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());
            }
        }
    }

}