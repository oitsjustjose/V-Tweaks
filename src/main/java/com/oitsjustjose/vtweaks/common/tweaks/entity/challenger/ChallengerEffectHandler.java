package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = LivingHurtEvent.class, category = "entity")
public class ChallengerEffectHandler extends VTweak {
    @Override
    public void process(Event event) {
        var evt = (LivingHurtEvent) event;
        if (evt.getEntity() == null || evt.getSource() == null) return;

        if (evt.getSource().getDirectEntity() instanceof Monster monster) {
            if (ChallengerHelpers.hasChallengerEntityModifier(monster)) {
                var challenger = ChallengerHelpers.getChallengerEntityModifier(monster);
                if (challenger == null) return;

                var eff = challenger.getAttackEffects();
                eff.forEach(x -> evt.getEntity().addEffect(x));
            }
        }
    }

//    @Override
//    public boolean isForEvent(Event event) {
//        return event instanceof LivingHurtEvent;
//    }
}
