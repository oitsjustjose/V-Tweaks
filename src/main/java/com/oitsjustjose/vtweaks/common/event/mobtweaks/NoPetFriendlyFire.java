package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NoPetFriendlyFire {
    @SubscribeEvent
    public void registerEvent(AttackEntityEvent evt) {
        if (MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.DISABLED) {
            return;
        }

        if (evt.getPlayer() == null || evt.getTarget() == null) {
            return;
        }

        if (!(evt.getTarget() instanceof TameableEntity)) {
            return;
        }

        TameableEntity pet = (TameableEntity) evt.getTarget();
        if (!pet.isTamed()) {
            return;
        }

        VTweaks.getInstance().LOGGER.info("Pet is tamed to {}", pet.getOwner());

        boolean applyToAny = MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.ALL;
        if (applyToAny || pet.getOwner() == evt.getPlayer()) {
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }
}
