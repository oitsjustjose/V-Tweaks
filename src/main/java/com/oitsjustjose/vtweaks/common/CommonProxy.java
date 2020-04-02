package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;
import com.oitsjustjose.vtweaks.common.network.ParticlePacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class CommonProxy
{
    public static NetworkManager networkManager = new NetworkManager();

    public void init()
    {
        networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleServer);
        networkManager.networkWrapper.registerMessage(1, ParticlePacket.class, ParticlePacket::encode,
                ParticlePacket::decode, ParticlePacket::handleServer);
    }

    public void playSound(PlayerEntity player)
    {
        ArmorBreakPacket msg = new ArmorBreakPacket();
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
    }

    public void hurt(PlayerEntity player, float newHealth)
    {
        player.setHealth(newHealth);
    }

    public void showParticle(double x, double y, double z, float r, float g, float b)
    {
        ParticlePacket msg = new ParticlePacket(x, y, z, r, g, b);
        networkManager.networkWrapper.send(PacketDistributor.ALL.noArg(), msg);
    }
}