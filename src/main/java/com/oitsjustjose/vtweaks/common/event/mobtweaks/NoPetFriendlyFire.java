package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
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

        if (!(evt.getTarget() instanceof TamableAnimal)) {
            return;
        }

        TamableAnimal pet = (TamableAnimal) evt.getTarget();
        if (!pet.isTame()) {
            return;
        }

        boolean applyToAny = MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.ALL;
        if (applyToAny || pet.getOwner() == evt.getPlayer()) {
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }

    /**
     * Prevents Auxilliary Damage to Pets
     */
    @SubscribeEvent
    public void registerEvent(LivingHurtEvent evt) {
        if (MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.DISABLED) {
            return;
        }

        if (evt.getSource() == null || evt.getEntity() == null) {
            return;
        }

        // Check that the main target of pain is a pet
        if (!(evt.getEntity() instanceof TamableAnimal)) {
            return;
        }

        // Check if the main source of pain is a player
        if (!(evt.getSource().getDirectEntity() instanceof Player)) {
            return;
        }

        TamableAnimal pet = (TamableAnimal) evt.getEntity();
        Player player = (Player) evt.getSource().getDirectEntity();

        boolean applyToAny = MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.ALL;

        if (applyToAny || pet.isOwnedBy(player)) {
            evt.setAmount(0);
            evt.setResult(Event.Result.DENY);
            if (evt.isCancelable()) {
                evt.setCanceled(true);
            }
        }
    }
}
