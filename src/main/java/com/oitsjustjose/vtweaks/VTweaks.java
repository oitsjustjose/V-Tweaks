package com.oitsjustjose.vtweaks;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.core.TickScheduler;
import com.oitsjustjose.vtweaks.common.core.TweakRegistry;
import com.oitsjustjose.vtweaks.common.data.challenger.ChallengerDataLoader;
import com.oitsjustjose.vtweaks.common.data.culling.EntityCullingDataLoader;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeSerializers;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MOD_ID)
public class VTweaks {
    private static VTweaks instance;
    public final Logger LOGGER = LogManager.getLogger();

    public final TweakRegistry TweakRegistry = new TweakRegistry();
    public final TickScheduler Scheduler = new TickScheduler();
    public final NetworkManager NetworkManager = new NetworkManager();

    public VTweaks(IEventBus eventBus, ModContainer modContainer) {
        instance = this;

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(Scheduler);

        ModRecipeTypes.RECIPE_TYPES.register(eventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(eventBus);

        eventBus.register(NetworkManager);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "vtweaks-common.toml");
    }

    public static VTweaks getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onSlashReload(AddReloadListenerEvent evt) {
        evt.addListener(new ChallengerDataLoader());
        evt.addListener(new EntityCullingDataLoader());
    }
}