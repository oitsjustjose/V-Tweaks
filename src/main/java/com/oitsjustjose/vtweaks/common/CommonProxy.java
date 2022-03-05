package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.DustParticlePacket;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();

    public void init() {
        networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleServer);
        networkManager.networkWrapper.registerMessage(1, DustParticlePacket.class, DustParticlePacket::encode,
                DustParticlePacket::decode, DustParticlePacket::handleServer);
    }

    public void playSound(Player player) {
        ArmorBreakPacket msg = new ArmorBreakPacket();
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }

    public void addParticle(float r, float g, float b, double x, double y, double z) {
        DustParticlePacket msg = new DustParticlePacket(r, g, b, x, y, z);
        networkManager.networkWrapper.send(PacketDistributor.ALL.noArg(), msg);
    }
}