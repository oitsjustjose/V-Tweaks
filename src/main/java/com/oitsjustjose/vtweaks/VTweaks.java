package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.config.MixinConfig;
import com.oitsjustjose.vtweaks.common.core.TickScheduler;
import com.oitsjustjose.vtweaks.common.core.TweakRegistry;
import com.oitsjustjose.vtweaks.common.data.challenger.ChallengerDataLoader;
import com.oitsjustjose.vtweaks.common.data.culling.EntityCullingDataLoader;
import com.oitsjustjose.vtweaks.common.registries.RecipeTypeRegistry;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.integration.jei.JeiPlugin;
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

@Mod(Constants.MOD_ID)
public class VTweaks {
    private static VTweaks instance;
    public static final CommonProxy Proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public final Logger LOGGER = LogManager.getLogger();
    public final TweakRegistry TweakRegistry = new TweakRegistry();
    public final TickScheduler Scheduler = new TickScheduler();
    public final RecipeTypeRegistry CustomRecipeRegistry = new RecipeTypeRegistry();

    public VTweaks() {
        instance = this;

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(Scheduler);
        MinecraftForge.EVENT_BUS.register(TweakRegistry);

        CustomRecipeRegistry.SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        this.configSetup();
    }

    public static VTweaks getInstance() {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent ignoredEvent) {
        Proxy.init();
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-common.toml"));
        ClientConfig.loadConfig(ClientConfig.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-client.toml"));
        MixinConfig.loadConfig(MixinConfig.MIXIN_CONFIG, FMLPaths.CONFIGDIR.get().resolve("vtweaks-mixins.toml"));
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new ChallengerDataLoader());
        evt.addListener(new EntityCullingDataLoader());
        JeiPlugin.AllAnvilRecipes.clear();
        JeiPlugin.AllFluidConversionRecipes.clear();
    }
}