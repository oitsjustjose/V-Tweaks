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
        if (MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.DISABLED) return;
        if (evt.getTarget() == null) return;
        if (evt.getEntity() == null) return;
        if (!(evt.getTarget() instanceof TamableAnimal pet)) return;
        if (!pet.isTame()) return;

        boolean applyToAny = MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.ALL;
        if (applyToAny || pet.getOwner() == evt.getEntity()) {
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
        if (MobTweakConfig.NO_PET_FRIENDLY_FIRE.get() == MobTweakConfig.NoPetFriendlyFire.DISABLED) return;
        if (evt.getSource() == null) return;
        if (evt.getEntity() == null) return;
        if (!(evt.getEntity() instanceof TamableAnimal pet)) return;
        if (!(evt.getSource().getDirectEntity() instanceof Player player)) return;

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
