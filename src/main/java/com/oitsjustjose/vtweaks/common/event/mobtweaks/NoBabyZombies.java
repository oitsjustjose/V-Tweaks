package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoBabyZombies {
    @SubscribeEvent
    public void registerEvent(LivingSpawnEvent event) {
        if (!MobTweakConfig.DISABLE_BABY_ZOMBIES.get()) {
            return;
        }

        if (event.getEntity() == null) {
            return;
        }

        if (event.getEntity() instanceof Zombie) {
            Zombie z = (Zombie) event.getEntity();
            if (z.isBaby()) {
                z.setBaby(false);
            }
        }
    }
}
