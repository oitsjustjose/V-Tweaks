package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ClientProxy extends CommonProxy {
    Minecraft mc;

    public static void showDustParticle(float r, float g, float b, double x, double y, double z) {
        try {
            Minecraft mc = Minecraft.getInstance();
            Entity view = mc.getCameraEntity();

            if (view != null) {
                Vec3 position = view.getForward();
                Vec3 particlePos = new Vec3(x, y, z);

                if (mc.player != null && mc.player.shouldRenderAtSqrDistance(position.distanceTo(particlePos))) {
                    ParticleOptions p = new DustParticleOptions(new Vector3f(r, g, b), 1.0F);
                    mc.particleEngine.createParticle(p, x, y, z, 0D, 0D, 0D);
                }
            }
        } catch (IllegalStateException Exception) {
            VTweaks.getInstance().LOGGER.warn("IllegalStateException thrown in ClientProxy#showDustParticle. Probably random source?");
            Exception.printStackTrace();
        }
    }

    @Override
    public void init() {
        mc = Minecraft.getInstance();
        networkManager.networkWrapper.registerMessage(0, ChallengerParticlePacket.class, ChallengerParticlePacket::encode, ChallengerParticlePacket::decode, ChallengerParticlePacket::handleClient);
    }

    @Override
    public void addParticle(float r, float g, float b, double x, double y, double z) {
        showDustParticle(r, g, b, x, y, z);
    }
}