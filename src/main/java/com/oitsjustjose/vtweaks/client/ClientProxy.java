package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init()
    {
    }

    @Override
    public void hurt(PlayerEntity player, float newHealth)
    {
        player.setHealth(newHealth);
        player.performHurtAnimation();
    }

    @Override
    public void playSoundClient(SoundEvent event, float volume, float pitch)
    {
        Minecraft.getInstance().player.playSound(event, volume, pitch);
    }
}