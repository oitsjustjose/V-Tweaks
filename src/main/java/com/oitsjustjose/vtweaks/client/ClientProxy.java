package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticleData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class ClientProxy {
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
        } catch (IllegalStateException Exception) {
            VTweaks.getInstance().LOGGER.warn("IllegalStateException thrown in ClientProxy#showDustParticle. Probably random source?");
            Exception.printStackTrace();
        }
    }
}