package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import java.util.Random;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles {
    public static final String CHALLENGER_PARTICLE_KEY = "challenger_mobs_last_particle";

    @SubscribeEvent
    public void registerEvent(EntityEvent evt) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || !MobTweakConfig.ENABLE_CHALLENGER_PARTICLES.get()) {
            return;
        }

        if (evt.getEntity() == null || !evt.getEntity().isAlive()) {
            return;
        }

        if (evt.getEntity() instanceof MonsterEntity) {
            if (ChallengerMobs.isChallengerMob((MonsterEntity) evt.getEntity())) {
                MonsterEntity monster = (MonsterEntity) evt.getEntity();
                ChallengerMobType type = ChallengerMobs.getChallengerMobType(monster);

                Random rand = monster.getRNG();
                CompoundNBT comp = monster.getPersistentData();

                if (comp.contains(CHALLENGER_PARTICLE_KEY)) {
                    if (System.currentTimeMillis() - comp.getLong(CHALLENGER_PARTICLE_KEY) < 10L) {
                        return;
                    }
                } else {
                    comp.putLong(CHALLENGER_PARTICLE_KEY, System.currentTimeMillis());
                }

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
                    VTweaks.proxy.showParticle(x, y, z, r, g, b);
                }
            }
        }
    }
}