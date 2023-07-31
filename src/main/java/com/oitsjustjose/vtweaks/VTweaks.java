package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.client.ClientProxy;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.core.TickScheduler;
import com.oitsjustjose.vtweaks.common.core.TweakRegistry;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.challenger.ChallengerDataLoader;
import com.oitsjustjose.vtweaks.common.data.culling.EntityCullingDataLoader;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.registries.RecipeTypeRegistry;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
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

@Mod(Constants.MOD_ID)
public class VTweaks {
    public static final CommonProxy Proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static VTweaks instance;
    public final Logger LOGGER = LogManager.getLogger();
    public final TweakRegistry TweakRegistry = new TweakRegistry();
    public final TickScheduler Scheduler = new TickScheduler();
    public final RecipeTypeRegistry CustomRecipeRegistry = new RecipeTypeRegistry();

    private final HashMap<ResourceLocation, AnvilRecipe> AllAnvilRecipes;
    private final HashMap<ResourceLocation, FluidConversionRecipe> AllFluidConversionRecipes;

    public VTweaks() {
        instance = this;

        AllAnvilRecipes = new HashMap<>();
        AllFluidConversionRecipes = new HashMap<>();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(Scheduler);

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
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new ChallengerDataLoader());
        evt.addListener(new EntityCullingDataLoader());
        AllAnvilRecipes.clear();
        AllFluidConversionRecipes.clear();
    }

    public void addAnvilRecipe(ResourceLocation loc, AnvilRecipe recipe) {
        this.AllAnvilRecipes.put(loc, recipe);
    }

    public void addFluidConversionRecipe(ResourceLocation loc, FluidConversionRecipe recipe) {
        this.AllFluidConversionRecipes.put(loc, recipe);
    }

    @SuppressWarnings("unchecked")
    public HashMap<ResourceLocation, AnvilRecipe> getAnvilRecipes() {
        return (HashMap<ResourceLocation, AnvilRecipe>) this.AllAnvilRecipes.clone();
    }

    @SuppressWarnings("unchecked")
    public HashMap<ResourceLocation, FluidConversionRecipe> getFluidConversionRecipes() {
        return (HashMap<ResourceLocation, FluidConversionRecipe>) this.AllFluidConversionRecipes.clone();
    }
}