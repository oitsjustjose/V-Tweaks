package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.client.LowHealthSound;
import com.oitsjustjose.vtweaks.client.SmallBees;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.common.enchantment.EnchantmentImperishable;
import com.oitsjustjose.vtweaks.common.enchantment.EnchantmentLumbering;
import com.oitsjustjose.vtweaks.common.enchantment.FeatherFallingTweak;
import com.oitsjustjose.vtweaks.common.enchantment.handler.EnchantmentImperishableHandler;
import com.oitsjustjose.vtweaks.common.enchantment.handler.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.common.event.DeathPoint;
import com.oitsjustjose.vtweaks.common.event.StormTweak;
import com.oitsjustjose.vtweaks.common.event.ToolTips;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.BonemealTweaks;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.CakeTweak;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.ChopDown;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.CropHelper;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.AnvilRepairTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.ConcreteTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.DropTweaks;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerMobs;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerParticles;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.FeatherPlucker;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.NoPetFriendlyFire;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.PeacefulSurface;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.PetArmory;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.Recipes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class VTweaks {
    private static VTweaks instance;
    public Logger LOGGER = LogManager.getLogger();
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static Enchantment lumbering = new EnchantmentLumbering();
    public static Enchantment imperishable = new EnchantmentImperishable();

    public VTweaks() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        this.configSetup();
    }

    public static VTweaks getInstance() {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent event) {
        // Proxy init - sets up networking too
        proxy.init();

        // Enchantments
        MinecraftForge.EVENT_BUS.register(new Recipes());

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new NoPetFriendlyFire());
        MinecraftForge.EVENT_BUS.register(new SmallBees());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobs());
        MinecraftForge.EVENT_BUS.register(new ChallengerParticles());
        MinecraftForge.EVENT_BUS.register(new PeacefulSurface());

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ChopDown());

        // Item Tweaks
        MinecraftForge.EVENT_BUS.register(new DropTweaks());
        MinecraftForge.EVENT_BUS.register(new ConcreteTweaks());
        ConcreteTweaks.powderBlocks.forEach((item) -> {
            DispenserBlock.registerBehavior(item, ConcreteTweaks.CONCRETE_POWDER_BEHAVIOR_DISPENSE_ITEM);
        });

        // Miscellaneous Features
        MinecraftForge.EVENT_BUS.register(new ToolTips());
        MinecraftForge.EVENT_BUS.register(new StormTweak());
        MinecraftForge.EVENT_BUS.register(new DeathPoint());
        MinecraftForge.EVENT_BUS.register(new AnvilRepairTweaks());
        MinecraftForge.EVENT_BUS.register(new LowHealthSound());
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-common.toml"));
        ClientConfig.loadConfig(ClientConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-client.toml"));
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerEnchantments(final RegistryEvent.Register<Enchantment> enchantmentRegistryEvent) {

            if (EnchantmentConfig.ENABLE_LUMBERING.get()) {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.lumbering);
                MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
            }

            if (EnchantmentConfig.ENABLE_IMPERISHABLE.get()) {
                enchantmentRegistryEvent.getRegistry().register(VTweaks.imperishable);
                MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
            }

            if (EnchantmentConfig.ENABLE_FF_TWEAK.get()) {
                MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());
            }
        }
    }

}