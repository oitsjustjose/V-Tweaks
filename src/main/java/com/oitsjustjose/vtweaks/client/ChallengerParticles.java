package com.oitsjustjose.vtweaks.client;

import java.util.Random;

import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles {
    private final long lastParticle = 0L;
    private final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(RenderLevelLastEvent evt) {
        if (mc.level == null) {
            return;
        }

        if (System.currentTimeMillis() - lastParticle < 10L) {
            return;
        }

        Entity view = mc.getCameraEntity();
        if (view == null) {
            return;
        }
        Vec3 position = view.getForward();

        ClientProxy.challengerMobs.forEach((id, type) -> {
            Entity tmp = mc.level.getEntity(id);
            if (tmp instanceof Monster) {
                Monster monster = (Monster) tmp;
                if (monster.shouldRenderAtSqrDistance(position.distanceTo(monster.getForward())) && monster.isAlive()) {
                    Random rand = monster.getRandom();
                    float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                    float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                    double x = monster.getX() + noiseX;
                    double y = rand.nextBoolean() ? monster.getY() + (monster.getBbHeight() / 2) : monster.getY();
                    double z = monster.getZ() + noiseZ;

                    y += rand.nextFloat() + rand.nextInt(1);

                    if (type != null) {
                        mc.levelRenderer.addParticle(type.getParticle(), false, x, y, z, 0D, 0D, 0D);
                    }
                }
            }
        });

        ClientProxy.challengerMobs.clear();
    }
}
