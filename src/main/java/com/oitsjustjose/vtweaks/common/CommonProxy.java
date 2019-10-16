package com.oitsjustjose.vtweaks.common;

import com.oitsjustjose.vtweaks.common.network.CropHelperPacket;
import com.oitsjustjose.vtweaks.common.network.NetworkManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class CommonProxy
{
    public static NetworkManager networkManager = new NetworkManager();

    public void init()
    {
        networkManager.networkWrapper.registerMessage(0, CropHelperPacket.class, CropHelperPacket::encode,
                CropHelperPacket::decode, CropHelperPacket::handleServer);
    }

    public void swingArm(PlayerEntity player, boolean shouldSwing)
    {
        CropHelperPacket msg = new CropHelperPacket(shouldSwing);
        networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
    }

    public void hurt(PlayerEntity player, int newHealth)
    {
        player.setHealth(newHealth);
    }
}