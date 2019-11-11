package com.oitsjustjose.vtweaks.client;

import java.util.Random;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerMobType;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles
{
    private long last = System.currentTimeMillis();

    @SubscribeEvent
    public void registerEvent(RenderWorldLastEvent event)
    {
        if (!(System.currentTimeMillis() - last >= 10L))
        {
            return;
        }

        if (Minecraft.getInstance().isGamePaused())
        {
            return;
        }

        last = System.currentTimeMillis();

        VTweaks.getInstance().challengerMobs.forEach((monster, id) -> {
            if (!monster.isAlive())
            {
                VTweaks.getInstance().challengerMobs.remove(monster);
            }
            else
            {
                Random rand = monster.getRNG();

                float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                double x = monster.posX + noiseX;
                double y = monster.posY + rand.nextFloat() + rand.nextInt(1);
                double z = monster.posZ + noiseZ;

                ChallengerMobType type = getChallengerMobType(monster);

                if (type != null)
                {
                    float r = type.getRed();
                    float g = type.getGreen();
                    float b = type.getBlue();

                    IParticleData redstoneParticle = new RedstoneParticleData(r, g, b, 1F);

                    Minecraft.getInstance().worldRenderer.addParticle(redstoneParticle, false, x, y, z, 0D, 0D, 0D);
                }
            }
        });
    }

    private ChallengerMobType getChallengerMobType(MonsterEntity monster)
    {
        CompoundNBT comp = monster.getPersistentData();

        if (comp.contains("challenger_mob_data"))
        {
            CompoundNBT cmd = comp.getCompound("challenger_mob_data");
            String type = cmd.getString("variant");
            for (ChallengerMobType t : ChallengerMobType.values())
            {
                if (t.getPrefix().equalsIgnoreCase(type))
                {
                    return t;
                }
            }
        }
        return null;
    }
}