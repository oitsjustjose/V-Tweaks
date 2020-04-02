package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.network.ArmorBreakPacket;
import com.oitsjustjose.vtweaks.common.network.ParticlePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.SoundEvents;

public class ClientProxy extends CommonProxy
{
    Minecraft mc;
    
    @Override
    public void init()
    {
        mc = Minecraft.getInstance();
        CommonProxy.networkManager.networkWrapper.registerMessage(0, ArmorBreakPacket.class, ArmorBreakPacket::encode,
                ArmorBreakPacket::decode, ArmorBreakPacket::handleClient);
        networkManager.networkWrapper.registerMessage(1, ParticlePacket.class, ParticlePacket::encode,
                ParticlePacket::decode, ParticlePacket::handleClient);
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
        mc.player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1F, 1F);
    }

    @Override
    public void showParticle(double x, double y, double z, float r, float g, float b)
    {
        if (MobTweakConfig.ENABLE_CHALLENGER_PARTICLES.get())
        {
            IParticleData redstoneParticle = new RedstoneParticleData(r, g, b, 1F);
            mc.worldRenderer.addParticle(redstoneParticle, false, x, y, z, 0D, 0D, 0D);
        }
    }
}