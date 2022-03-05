package com.oitsjustjose.vtweaks.common.entity;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.CommonProxy;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import com.oitsjustjose.vtweaks.common.entity.ChallengerMob;
import com.oitsjustjose.vtweaks.common.entity.ChallengerMobHandler;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ChallengerParticles {

    @SubscribeEvent
    public void registerEvent(EntityEvent evt) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || !ClientConfig.ENABLE_CHALLENGER_PARTICLES.get()) {
            return;
        }

        if (evt.getEntity() == null || !evt.getEntity().isAlive()) {
            return;
        }


        if (evt.getEntity() instanceof MonsterEntity) {
            if (ChallengerMobHandler.isChallengerMob((MonsterEntity) evt.getEntity())) {
                MonsterEntity monster = (MonsterEntity) evt.getEntity();
                ChallengerMob type = ChallengerMobHandler.getChallengerMob(monster);

                if (monster.isAlive() && type != null) {
                    Random rand = monster.getRNG();
                    float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                    float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                    double x = monster.getPosX() + noiseX;
                    double y = rand.nextBoolean() ? monster.getPosY() + (monster.getHeight() / 2) : monster.getPosY();
                    double z = monster.getPosZ() + noiseZ;
                    y += rand.nextFloat() + rand.nextInt(1);

                    Vector3f color = type.getParticleColor();
                    VTweaks.proxy.addParticle(color.getX(), color.getY(), color.getZ(), x, y, z);
                }
            }
        }
    }
}