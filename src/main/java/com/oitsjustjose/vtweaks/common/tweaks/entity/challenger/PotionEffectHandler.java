package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import com.oitsjustjose.vtweaks.common.tweaks.core.VTweak;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = LivingHurtEvent.class, category = "entity")
public class PotionEffectHandler extends VTweak {
    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
    }

    @Override
    public void process(Event event) {
        var evt = (LivingHurtEvent) event;
        if (evt.getEntity() == null || evt.getSource() == null) return;

        if (evt.getSource().getDirectEntity() instanceof Monster monster) {
            if (Helpers.hasChallengerEntityModifier(monster)) {
                var challenger = Helpers.getChallengerEntityModifier(monster);
                if (challenger == null) return;

                var eff = challenger.getAttackEffects();
                eff.forEach(x -> evt.getEntity().addEffect(x));
            }
        }
    }

    @Override
    public boolean isForEvent(Event event) {
        return event instanceof LivingHurtEvent;
    }
}
