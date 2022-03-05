package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.DustParticlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ClientProxy extends CommonProxy {
    Minecraft mc;
    public static ConcurrentHashMap<Integer, ChallengerMob> challengerMobs = new ConcurrentHashMap<>();

    @Override
    public void init() {
        mc = Minecraft.getInstance();
        CommonProxy.networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleClient);
        networkManager.networkWrapper.registerMessage(1, DustParticlePacket.class, DustParticlePacket::encode,
                DustParticlePacket::decode, DustParticlePacket::handleClient);
    }

    @Override
    public void playSound(PlayerEntity player) {
        if (mc.player != null) {
            mc.player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1F, 1F);
        }
    }

    public void addParticle(float r, float g, float b, double x, double y, double z) {
        showDustParticle(r, g, b, x, y, z);
    }

    public static void showDustParticle(float r, float g, float b, double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.getRenderViewEntity() != null) {
            Vector3d viewPos = mc.getRenderViewEntity().getPositionVec();
            Vector3d particlePos = new Vector3d(x, y, z);

            if (mc.player != null && mc.player.isInRangeToRenderDist(viewPos.squareDistanceTo(particlePos))) {
                IParticleData p = new RedstoneParticleData(r, g, b, 1.0F);
                mc.worldRenderer.addParticle(p, false, x, y, z, 0D, 0D, 0D);
            }
        }
    }
}