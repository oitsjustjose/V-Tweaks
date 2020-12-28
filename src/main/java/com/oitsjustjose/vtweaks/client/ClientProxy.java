package com.oitsjustjose.vtweaks.client;

import java.util.concurrent.ConcurrentHashMap;

import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerMobType;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.ChallengerMobPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    Minecraft mc;
    public static ConcurrentHashMap<Integer, ChallengerMobType> challengerMobs = new ConcurrentHashMap<>();

    @Override
    public void init() {
        mc = Minecraft.getInstance();
        CommonProxy.networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleClient);
        networkManager.networkWrapper.registerMessage(1, ChallengerMobPacket.class, ChallengerMobPacket::encode,
                ChallengerMobPacket::decode, ChallengerMobPacket::handleClient);
        MinecraftForge.EVENT_BUS.register(new ChallengerParticles());
    }

    @Override
    public void hurt(PlayerEntity player, float newHealth) {
        player.setHealth(newHealth);
        player.performHurtAnimation();
    }

    @Override
    public void playSound(PlayerEntity player) {
        mc.player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1F, 1F);
    }

    @Override
    public void addChallengerMob(MonsterEntity entity, ChallengerMobType type) {
        challengerMobs.put(entity.getEntityId(), type);
    }
}