package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import com.oitsjustjose.vtweaks.common.tweaks.core.VTweak;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Event;

/* Category is Client for the config, maybe category should be renamed Config Category? */
@Tweak(eventClass = EntityEvent.class, category = "client")
public class ParticleHandler extends VTweak {
    private ForgeConfigSpec.BooleanValue enable;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enable = builder.comment("Enable colored particles for challenger mobs (dependent on particle setting as well)").define("enableChallengerMobParticles", true);
    }

    @Override
    public void process(Event event) {
        if (!this.enable.get()) return;

        var evt = (EntityEvent) event;
        if (evt.getEntity() == null) return;
        if (!evt.getEntity().isAlive()) return;
        if (!(evt.getEntity() instanceof Monster monster)) return;

        var modifier = Helpers.getChallengerEntityModifier(monster);
        if (modifier != null) {
            var rand = monster.getRandom();
            var noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
            var noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

            var x = monster.getX() + noiseX;
            var y = rand.nextBoolean() ? monster.getY() + (monster.getBbHeight() / 2) : monster.getY();
            var z = monster.getZ() + noiseZ;
            y += rand.nextFloat() + rand.nextInt(1);

            var color = modifier.getParticleColor();
            VTweaks.Proxy.addParticle(color.x(), color.y(), color.z(), x, y, z);
        }
    }

    @Override
    public boolean isForEvent(Event event) {
        return event instanceof EntityEvent;
    }
}
