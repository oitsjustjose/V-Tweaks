package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.ChallengerMobPacket;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();

    public void init() {
        networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleServer);
        networkManager.networkWrapper.registerMessage(1, ChallengerMobPacket.class, ChallengerMobPacket::encode,
                ChallengerMobPacket::decode, ChallengerMobPacket::handleServer);
    }

    public void playSound(PlayerEntity player) {
        ArmorBreakPacket msg = new ArmorBreakPacket();
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
    }

    public void hurt(PlayerEntity player, float newHealth) {
        player.setHealth(newHealth);
    }

    public void addChallengerMob(MonsterEntity entity, ChallengerMob challenger) {
        ChallengerMobPacket msg = new ChallengerMobPacket(entity.getEntityId(), challenger);
        networkManager.networkWrapper.send(PacketDistributor.ALL.noArg(), msg);
    }
}