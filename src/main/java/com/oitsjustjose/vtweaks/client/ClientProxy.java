package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.network.CropHelperPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        CommonProxy.networkManager.networkWrapper.registerMessage(0, CropHelperPacket.class, CropHelperPacket::encode,
                CropHelperPacket::decode, CropHelperPacket::handleClient);
    }

    @Override
    public void swingArm(PlayerEntity player, boolean shouldSwing)
    {
        if (shouldSwing)
        {
            Minecraft.getInstance().enqueue(() -> {
                Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
            });
        }
    }
}