package com.oitsjustjose.vtweaks.common.tweaks.entity.culling;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EntityCullingRule {
    private final List<Biome> biomes;
    private final List<TagKey<Biome>> biomeTags;
    private final List<ResourceLocation> dimensions;
    private final List<TagKey<EntityType<?>>> entityTypeTags;
    private final List<ResourceLocation> entityTypes;

    public EntityCullingRule(List<String> biomeFilterRaw, List<String> dimFilterRaw, List<String> entityFilterRaw) {
        biomes = new ArrayList<>();
        biomeTags = new ArrayList<>();
        dimensions = new ArrayList<>();
        entityTypeTags = new ArrayList<>();
        entityTypes = new ArrayList<>();

        biomeFilterRaw.forEach(x -> {
            try {
                if (x.startsWith("#")) {
                    var location = new ResourceLocation(x.substring(1));
                    var tagKey = TagKey.create(Registries.BIOME, location);
                    this.biomeTags.add(tagKey);
                } else {
                    var location = new ResourceLocation(x);
                    var b = ForgeRegistries.BIOMES.getValue(location);
                    if (b != null) {
                        this.biomes.add(b);
                    }
                }
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in biomes object {}: {}", x, e);
            }
        });

        dimFilterRaw.forEach(x -> {
            try {
                var location = new ResourceLocation(x);
                this.dimensions.add(location);
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in dimensions object {}: {}", x, e);
            }
        });

        entityFilterRaw.forEach(x -> {
            try {
                // Case that you've given me a tag
                if (x.startsWith("#")) {
                    var location = new ResourceLocation(x.substring(1));
                    var tagKey = TagKey.create(Registries.ENTITY_TYPE, location);
                    this.entityTypeTags.add(tagKey);
                } else {
                    var location = new ResourceLocation(x);
                    var ent = ForgeRegistries.ENTITY_TYPES.getValue(location);
                    if (ent != null) {
                        this.entityTypes.add(location);
                    }
                }
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in entities object {}: {}", x, e);
            }
        });
    }

    public boolean apply(MobSpawnEvent.FinalizeSpawn evt) {
        // filter by entity
        var hasMatchedOnEntity = this.entityTypes.contains(ForgeRegistries.ENTITY_TYPES.getKey(evt.getEntity().getType()));
        var hasMatchedOnEntityType = this.entityTypeTags.stream().anyMatch(typeTag -> evt.getEntity().getType().is(typeTag));
        if (!hasMatchedOnEntity && !hasMatchedOnEntityType) return false;

        var lvl = (ServerLevel) evt.getLevel();
        var dimName = lvl.dimension().location();
        var biomeHolder = lvl.getBiome(evt.getEntity().blockPosition());
        var unwrappedBiome = biomeHolder.unwrap();

        var hasMatchedOnDim = dimensions.isEmpty() || dimensions.contains(dimName);
        var hasMatchedOnBiomeTag = biomeTags.isEmpty() || biomeTags.stream().anyMatch(biomeHolder::is);
        var hasMatchedOnBiome = biomes.isEmpty() || biomes.stream().anyMatch(b -> {
            var eBiome = unwrappedBiome.right().orElse(null);
            if (eBiome != null) return this.biomes.contains(eBiome);

            var rb = unwrappedBiome.left().orElse(null);
            if (rb != null) {
                eBiome = ForgeRegistries.BIOMES.getValue(rb.location());
                if (eBiome != null) return this.biomes.contains(eBiome);
            }
            return false;
        });

        return (hasMatchedOnDim && (hasMatchedOnBiome || hasMatchedOnBiomeTag));
    }

    @Override
    public String toString() {
        return "EntityCullingRule for Entities" + this.entityTypes.toString() +
                " + " +
                this.entityTypeTags.toString() +
                " in Biomes " +
                this.biomes.toString() +
                " + " +
                this.biomeTags.toString() +
                " In " +
                this.dimensions;
    }
}
