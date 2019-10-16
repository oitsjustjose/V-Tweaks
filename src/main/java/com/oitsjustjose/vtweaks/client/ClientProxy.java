package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;

import net.minecraft.entity.player.PlayerEntity;

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
}