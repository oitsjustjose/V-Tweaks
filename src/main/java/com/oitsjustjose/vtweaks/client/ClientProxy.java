package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
        CommonProxy.networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleClient);

        MinecraftForge.EVENT_BUS.register(new ChallengerParticles());
    }

    @Override
    public void hurt(PlayerEntity player, float newHealth)
    {
        player.setHealth(newHealth);
        player.performHurtAnimation();
    }

    @Override
    public void playSound(PlayerEntity player)
    {
        Minecraft.getInstance().player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1F, 1F);
    }
}