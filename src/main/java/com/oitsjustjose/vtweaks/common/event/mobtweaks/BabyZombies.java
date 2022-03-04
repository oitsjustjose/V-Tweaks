package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BabyZombies {
    @SubscribeEvent
    public void registerEvent(LivingSpawnEvent event) {
        if (!MobTweakConfig.DISABLE_BABY_ZOMBIES.get()) {
            return;
        }

        if (event.getEntity() == null) {
            return;
        }

        if (event.getEntity() instanceof ZombieEntity) {
            ZombieEntity z = (ZombieEntity) event.getEntity();
            if (z.isChild()) {
                z.setChild(false);
            }
            return;
        }

        if (event.getEntity() instanceof ZombifiedPiglinEntity) {
            ZombifiedPiglinEntity z = (ZombifiedPiglinEntity) event.getEntity();
            if (z.isChild()) {
                z.setChild(false);
            }
            return;
        }
    }
}
