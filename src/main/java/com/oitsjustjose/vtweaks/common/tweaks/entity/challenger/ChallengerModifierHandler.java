package com.oitsjustjose.vtweaks.common.tweaks.entity.challenger;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = MobSpawnEvent.FinalizeSpawn.class, category = "entity")
public class ChallengerModifierHandler extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.DoubleValue globalChance;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("A data-driven way to make some special mobs with abilities, effects, specialized loot and more!").define("enableChallengerMobs", true);
        this.globalChance = builder.comment("This controls the overall chance for V-Tweaks to attempt converting a monster to a Challenger.\nThis chance is applied before any Challenger Mob weights or entity filters.").defineInRange("challengerMobGlobalChance", 0.25D, 0.0D, 1.0D);
    }

    @SubscribeEvent
    public void process(MobSpawnEvent.FinalizeSpawn evt) {
        if (!this.enabled.get()) return;
        if (this.globalChance.get() <= 0.0D) return;

        if (evt.getEntity() != null && evt.getEntity() instanceof Monster monster) {
            if (evt.getLevel().isClientSide()) return;
            if (evt.getEntity().getPersistentData().contains("challenger_mob_data")) return;
            if (evt.getLevel().getRandom().nextDouble() > this.globalChance.get()) return;
            if (ChallengerHelpers.hasChallengerEntityModifier(monster)) return;

            var modifier = ChallengerHelpers.filterForEntity(monster).pick();
            if (modifier != null && ChallengerHelpers.canBeChallenger(modifier, monster)) {
                modifier.apply(monster);
            }
        }
    }
}
