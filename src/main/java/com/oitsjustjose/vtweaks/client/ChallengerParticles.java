package com.oitsjustjose.vtweaks.client;

import java.util.Map.Entry;
import java.util.Random;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.event.mobtweaks.ChallengerMobType;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles {
    private long lastParticle = 0L;
    private Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(RenderWorldLastEvent evt) {
        if (System.currentTimeMillis() - lastParticle < 10L) {
            return;
        }

        Entity view = mc.getRenderViewEntity();
        if (view == null) {
            return;
        }
        Vector3d position = view.getPositionVec();

        ClientProxy.challengerMobs.forEach((id, type) -> {
            Entity tmp = mc.world.getEntityByID(id);
            if (tmp instanceof MonsterEntity) {
                MonsterEntity monster = (MonsterEntity) tmp;
                if (monster.isInRangeToRenderDist(position.squareDistanceTo(monster.getPositionVec()))
                        && monster.isAlive()) {
                    Random rand = monster.getRNG();
                    float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                    float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                    double x = monster.getPosX() + noiseX;
                    double y = rand.nextBoolean() ? monster.getPosY() + (monster.getHeight() / 2) : monster.getPosY();
                    double z = monster.getPosZ() + noiseZ;

                    y += rand.nextFloat() + rand.nextInt(1);

                    if (type != null) {
                        float r = type.getRed();
                        float g = type.getGreen();
                        float b = type.getBlue();
                        IParticleData particle = new RedstoneParticleData(r, g, b, 1F);
                        mc.worldRenderer.addParticle(particle, false, x, y, z, 0D, 0D, 0D);
                    }
                }
            }
        });

        ClientProxy.challengerMobs.clear();
    }
}
