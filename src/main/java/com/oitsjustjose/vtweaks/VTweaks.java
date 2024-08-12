package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.core.TickScheduler;
import com.oitsjustjose.vtweaks.common.core.TweakRegistry;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.challenger.ChallengerDataLoader;
import com.oitsjustjose.vtweaks.common.data.culling.EntityCullingDataLoader;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;
import com.oitsjustjose.vtweaks.common.registries.VtweaksRegistry;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(Constants.MOD_ID)
public class VTweaks {
    private static VTweaks instance;
    public final Logger LOGGER = LogManager.getLogger();
    public final TweakRegistry TweakRegistry = new TweakRegistry();
    public final TickScheduler Scheduler = new TickScheduler();
    public final NetworkManager NetworkManager = new NetworkManager();
    private final HashMap<ResourceLocation, AnvilRecipe> AllAnvilRecipes;
    private final HashMap<ResourceLocation, FluidConversionRecipe> AllFluidConversionRecipes;

    public VTweaks(IEventBus eventBus, ModContainer modContainer) {
        instance = this;

        AllAnvilRecipes = new HashMap<>();
        AllFluidConversionRecipes = new HashMap<>();
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(Scheduler);
        eventBus.register(NetworkManager);

        VtweaksRegistry.RECIPE_TYPES.register(eventBus);
        VtweaksRegistry.RECIPE_SERIALIZERS.register(eventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "vtweaks-client.toml");
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "vtweaks-common.toml");
    }

    public static VTweaks getInstance() {
        return instance;
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