package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.network.NetworkManager;
import com.oitsjustjose.vtweaks.common.network.packet.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticlePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class CommonProxy {
    public static final NetworkManager networkManager = new NetworkManager();

    public void init() {
        networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode, ArmorBreakPacket::decode, ArmorBreakPacket::handleServer);
        networkManager.networkWrapper.registerMessage(1, ChallengerParticlePacket.class, ChallengerParticlePacket::encode, ChallengerParticlePacket::decode, ChallengerParticlePacket::handleServer);
    }

    public void playSound(Player player) {
        var msg = new ArmorBreakPacket();
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }

    public void addParticle(float r, float g, float b, double x, double y, double z) {
        var msg = new ChallengerParticlePacket(r, g, b, x, y, z);
        networkManager.networkWrapper.send(PacketDistributor.ALL.noArg(), msg);
    }
}