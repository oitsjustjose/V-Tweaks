package com.oitsjustjose.vtweaks.common;

import net.minecraft.entity.player.PlayerEntity;

public class CommonProxy
{
    public void init()
    {
    }

    public void hurt(PlayerEntity player, float newHealth)
    {
        player.setHealth(newHealth);
    }
}