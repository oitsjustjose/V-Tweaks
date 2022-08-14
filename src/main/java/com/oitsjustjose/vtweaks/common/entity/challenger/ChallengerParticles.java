package com.oitsjustjose.vtweaks.common.entity.challenger;

import com.mojang.math.Vector3f;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles {
    @SubscribeEvent
    public void registerEvent(EntityEvent evt) {
        if (!MobTweakConfig.ENABLE_CHALLENGER_MOBS.get() || !ClientConfig.ENABLE_CHALLENGER_PARTICLES.get()) {
            return;
        }

        if (evt.getEntity() == null || !evt.getEntity().isAlive()) {
            return;
        }

        if (evt.getEntity() instanceof Monster) {
            if (ChallengerMobHandler.isChallengerMob((Monster) evt.getEntity())) {
                Monster monster = (Monster) evt.getEntity();
                ChallengerMob type = ChallengerMobHandler.getChallengerMob(monster);
                if (monster.isAlive() && type != null) {
                    RandomSource rand = monster.getRandom();
                    float noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
                    float noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

                    double x = monster.getX() + noiseX;
                    double y = rand.nextBoolean() ? monster.getY() + (monster.getBbHeight() / 2) : monster.getY();
                    double z = monster.getZ() + noiseZ;
                    y += rand.nextFloat() + rand.nextInt(1);

                    Vector3f color = type.getParticleColor();
                    VTweaks.proxy.addParticle(color.x(), color.y(), color.z(), x, y, z);
                }
            }
        }
    }
}