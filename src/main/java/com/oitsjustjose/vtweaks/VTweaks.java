package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.client.LowHealthSound;
import com.oitsjustjose.vtweaks.client.SmallBees;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.data.ChallengerMobDataLoader;
import com.oitsjustjose.vtweaks.common.data.EntityCullDataLoader;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipeHandler;
import com.oitsjustjose.vtweaks.common.enchantment.handler.FeatherFallingHandler;
import com.oitsjustjose.vtweaks.common.enchantment.handler.ImperishableHandler;
import com.oitsjustjose.vtweaks.common.enchantment.handler.LumberingHandler;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerMobHandler;
import com.oitsjustjose.vtweaks.common.entity.challenger.ChallengerParticles;
import com.oitsjustjose.vtweaks.common.entity.culling.EntityCullingHandler;
import com.oitsjustjose.vtweaks.common.event.DeathPoint;
import com.oitsjustjose.vtweaks.common.event.StormTweak;
import com.oitsjustjose.vtweaks.common.event.ToolTips;
import com.oitsjustjose.vtweaks.common.event.blocktweaks.*;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.AnvilRepairTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.ConcreteTweaks;
import com.oitsjustjose.vtweaks.common.event.itemtweaks.DropTweaks;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.*;
import com.oitsjustjose.vtweaks.common.registry.EnchantmentRegistrator;
import com.oitsjustjose.vtweaks.common.registry.RecipeRegistrator;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.JEICompat;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(Constants.MODID)
public class VTweaks {
    private static VTweaks instance;
    public Logger LOGGER = LogManager.getLogger();
    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static EnchantmentRegistrator Enchantments = new EnchantmentRegistrator();
    public static RecipeRegistrator RecipeSerializers = new RecipeRegistrator();

    public VTweaks() {
        instance = this;

        Enchantments.REGISTRATOR.register(FMLJavaModLoadingContext.get().getModEventBus());
        RecipeSerializers.SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

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
        MinecraftForge.EVENT_BUS.register(new LumberingHandler());
        MinecraftForge.EVENT_BUS.register(new ImperishableHandler());
        MinecraftForge.EVENT_BUS.register(new FeatherFallingHandler());
        MinecraftForge.EVENT_BUS.register(new AnvilRecipeHandler());

        // Mob Tweaks
        MinecraftForge.EVENT_BUS.register(new PetArmory());
        MinecraftForge.EVENT_BUS.register(new NoPetFriendlyFire());
        MinecraftForge.EVENT_BUS.register(new SmallBees());
        MinecraftForge.EVENT_BUS.register(new FeatherPlucker());
        MinecraftForge.EVENT_BUS.register(new ChallengerMobHandler());
        MinecraftForge.EVENT_BUS.register(new ChallengerParticles());
        MinecraftForge.EVENT_BUS.register(new EntityCullingHandler());
        MinecraftForge.EVENT_BUS.register(new PeacefulSurface());
        MinecraftForge.EVENT_BUS.register(new UngriefedCreepers());

        // Block Tweaks
        MinecraftForge.EVENT_BUS.register(new CropHelper());
        MinecraftForge.EVENT_BUS.register(new BonemealTweaks());
        MinecraftForge.EVENT_BUS.register(new CakeTweak());
        MinecraftForge.EVENT_BUS.register(new ChopDown());
        MinecraftForge.EVENT_BUS.register(new TorchLighting());

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

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new ChallengerMobDataLoader());
        evt.addListener(new EntityCullDataLoader());
        JEICompat.cache = new HashMap<>();
    }
}