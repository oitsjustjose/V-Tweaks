package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(eventClass = LivingSpawnEvent.CheckSpawn.class, category = "entity")
public class PeacefulSurfaceTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.IntValue minY;

    public static final TagKey<EntityType<?>> BLACKLISTED_ENTITIES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Constants.MOD_ID, "ignored_by_peaceful_surface"));
    public static final TagKey<DimensionType> BLACKLISTED_DIMENSIONS = TagKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Constants.MOD_ID, "peaceful_surface_blacklist_dims"));

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Prevents mobs from spawning above sea level unless it's a new moon").define("enablePeacefulSurface", false);
        this.minY = builder.comment("The lowest Y-level which mobs will be prevented from spawning").defineInRange("peacefulSurfaceMinY", 60, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    @SubscribeEvent
    public void process(LivingSpawnEvent.CheckSpawn evt) {
        if (!this.enabled.get()) return;

        if (evt.getEntity() == null) return;
        if (evt.getSpawnReason() != MobSpawnType.NATURAL) return;
        if (!(evt.getEntity() instanceof Monster) || evt.getEntity().getType().is(BLACKLISTED_ENTITIES)) return;
        if (!(evt.getLevel() instanceof ServerLevel level)) return;
        if (level.dimensionTypeRegistration().is(BLACKLISTED_DIMENSIONS)) return;

        int day = (int) (level.getLevelData().getDayTime() / 24000L % 2147483647L);
        if (!(day % 4 == 0 && day % 8 != 0)) { // Check for midnight
            if (evt.getEntity().getY() >= this.minY.get()) {
                evt.setResult(Event.Result.DENY);
            }
        }
    }
}
