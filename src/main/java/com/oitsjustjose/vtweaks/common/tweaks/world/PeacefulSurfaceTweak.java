package com.oitsjustjose.vtweaks.common.tweaks.world;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Tweak(category = "world.peacefulsurface")
public class PeacefulSurfaceTweak extends VTweak {
    public static final TagKey<EntityType<?>> BLACKLISTED_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, "ignored_by_peaceful_surface"));
    public static final TagKey<DimensionType> BLACKLISTED_DIMENSIONS = TagKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(Constants.MOD_ID, "peaceful_surface_blacklist_dims"));
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.IntValue minY;
    private ForgeConfigSpec.ConfigValue<List<String>> moonPhases;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        final String moonPhaseList = String.join(", ", Arrays.stream(MoonPhase.values()).map(Enum::toString).collect(Collectors.toList()));
        this.enabled = builder.comment("Prevents mobs from spawning above sea level unless it's a new moon").define("enablePeacefulSurface", false);
        this.minY = builder.comment("The lowest Y-level which mobs will be prevented from spawning").defineInRange("peacefulSurfaceMinY", 60, Integer.MIN_VALUE, Integer.MAX_VALUE);
        // *sigh* -- I really wanted this to be a list of ENUMs, but that doesn't seem to work right so I guess we'll just go with Strings... :/
        this.moonPhases = builder.comment("Peaceful Surface will only apply on nights whose moon phases are contained in this list (by default, all nights except for New Moons are going to be peaceful).\nOptions can include: " + moonPhaseList).define("moonPhasesToApplyTo",
                Lists.newArrayList(MoonPhase.FULL.toString(), MoonPhase.WANING_GIBBOUS.toString(), MoonPhase.LAST_QUARTER.toString(), MoonPhase.WANING_CRESCENT.toString(), MoonPhase.WAXING_CRESCENT.toString(), MoonPhase.FIRST_QUARTER.toString(), MoonPhase.WAXING_GIBBOUS.toString())
        );
        builder.pop();
    }

    @SubscribeEvent
    public void process(MobSpawnEvent.SpawnPlacementCheck evt) {
        if (!this.enabled.get()) return;

        if (evt.getSpawnType() != MobSpawnType.NATURAL) return;
        if (evt.getEntityType().getCategory() != MobCategory.MONSTER) return;
        if (evt.getEntityType().is(BLACKLISTED_ENTITIES)) return;
        if (!(evt.getLevel() instanceof ServerLevel level)) return;
        if (level.dimensionTypeRegistration().is(BLACKLISTED_DIMENSIONS)) return;

        // Check that the current moon phase is in the list of configured moon phases
        final String currentPhase = MoonPhase.values()[level.getMoonPhase()].toString();
        if (this.moonPhases.get().stream().noneMatch(moonPhase -> Objects.equals(moonPhase, currentPhase))) return;

        if (evt.getPos().getY() >= this.minY.get()) {
            evt.setResult(Event.Result.DENY);
        }
    }

    public enum MoonPhase {
        FULL, WANING_GIBBOUS, LAST_QUARTER, WANING_CRESCENT, NEW, WAXING_CRESCENT, FIRST_QUARTER, WAXING_GIBBOUS
    }
}
