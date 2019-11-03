package com.oitsjustjose.vtweaks.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;

public class CommonProxy
{
    public void init()
    {
    }

    public void hurt(PlayerEntity player, float newHealth)
    {
        player.setHealth(newHealth);
    }

    public void playSoundClient(SoundEvent event, float volume, float pitch)
    {

    }
}