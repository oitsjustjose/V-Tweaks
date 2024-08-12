package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.network.packet.ChallengerParticleData;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/* Category is Client for the config, maybe category should be renamed Config Category? */
@Tweak(category = "entity.challengers.particles")
public class ChallengerParticleHandler extends VTweak {
    private ModConfigSpec.BooleanValue enable;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enable = builder.comment("Enable colored particles for challenger mobs (dependent on particle setting as well)").define("enableChallengerMobParticles", true);
    }

    @SubscribeEvent
    public void process(EntityTickEvent.Pre evt) {
        if (!this.enable.get()) return;
        if (!evt.getEntity().isAlive()) return;
        if (!(evt.getEntity() instanceof Monster monster)) return;

        var modifier = ChallengerHelpers.getChallengerEntityModifier(monster);
        if (modifier != null) {
            var rand = monster.getRandom();
            var noiseX = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;
            var noiseZ = ((rand.nextBoolean() ? 1 : -1) * rand.nextFloat()) / 2;

            var x = monster.getX() + noiseX;
            var y = rand.nextBoolean() ? monster.getY() + (monster.getBbHeight() / 2) : monster.getY();
            var z = monster.getZ() + noiseZ;
            y += rand.nextFloat() + rand.nextInt(1);

            var color = modifier.getParticleColor();
            var particleData = new ChallengerParticleData(color.x(), color.y(), color.z(), x, y, z);
            if (FMLEnvironment.dist.isClient()) {
                com.oitsjustjose.vtweaks.client.ClientProxy.showDustParticle(particleData);
            } else {
                PacketDistributor.sendToAllPlayers(particleData);
            }
        }
    }
}
