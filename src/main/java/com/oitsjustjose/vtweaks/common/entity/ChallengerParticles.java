package com.oitsjustjose.vtweaks.common.entity;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChallengerParticles {
    public static final String CHALLENGER_PARTICLE_KEY = "challenger_mobs_last_particle";

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
                ChallengerMob challenger = ChallengerMobHandler.getChallengerMob(monster);
                if(challenger != null) {
                    VTweaks.proxy.addChallengerMob(monster, challenger);
                }
            }
        }
    }
}