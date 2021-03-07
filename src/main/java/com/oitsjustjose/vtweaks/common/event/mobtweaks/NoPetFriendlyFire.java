package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.Event;
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

    /** Prevents Auxilliary Damage to Pets */
    @SubscribeEvent
    public void registerEvent(LivingHurtEvent evt) {
        if (MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.DISABLED) {
            return;
        }

        if (evt.getSource() == null || evt.getEntity() == null) {
            return;
        }

        // Check that the main target of pain is a pet
        if (!(evt.getEntity() instanceof TameableEntity)) {
            return;
        }

        // Check if the main source of pain is a player
        if (!(evt.getSource().getTrueSource() instanceof PlayerEntity)) {
            return;
        }

        TameableEntity pet = (TameableEntity) evt.getEntity();
        PlayerEntity player = (PlayerEntity) evt.getSource().getTrueSource();

        boolean applyToAny = MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.ALL;

        if (applyToAny || pet.isOwner(player)) {
            evt.setAmount(0);
            evt.setResult(Event.Result.DENY);
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }
}
