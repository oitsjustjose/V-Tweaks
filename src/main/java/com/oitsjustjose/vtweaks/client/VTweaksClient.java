package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticleData;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.joml.Vector3f;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class VTweaksClient {
    public VTweaksClient(IEventBus eventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "vtweaks-client.toml");
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void showDustParticle(ChallengerParticleData data) {
        try {
            Minecraft mc = Minecraft.getInstance();
            Entity view = mc.getCameraEntity();

            if (view != null) {
                Vec3 position = view.getForward();
                Vec3 particlePos = new Vec3(data.x(), data.y(), data.z());

                if (mc.player != null && mc.player.shouldRenderAtSqrDistance(position.distanceTo(particlePos))) {
                    ParticleOptions p = new DustParticleOptions(new Vector3f(data.r(), data.g(), data.b()), 1.0F);
                    mc.particleEngine.createParticle(p, data.x(), data.y(), data.z(), 0D, 0D, 0D);
                }
            }
        } catch (IllegalStateException e) {
            VTweaks.getInstance().LOGGER.warn("IllegalStateException thrown in ClientProxy#showDustParticle. Probably random source? {}", e.getMessage());
        }
    }
}
