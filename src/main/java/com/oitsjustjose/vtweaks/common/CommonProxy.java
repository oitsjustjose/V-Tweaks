package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerMobType;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.ChallengerMobPacket;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();

    public void init() {
        networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleServer);
        networkManager.networkWrapper.registerMessage(1, ChallengerMobPacket.class, ChallengerMobPacket::encode,
                ChallengerMobPacket::decode, ChallengerMobPacket::handleServer);
    }

    public void playSound(Player player) {
        ArmorBreakPacket msg = new ArmorBreakPacket();
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
    }

    public void hurt(Player player, float newHealth) {
        player.setHealth(newHealth);
    }

    public void addChallengerMob(Monster entity, ChallengerMobType type) {
        ChallengerMobPacket msg = new ChallengerMobPacket(entity.getId(), type);
        networkManager.networkWrapper.send(PacketDistributor.ALL.noArg(), msg);
    }
}