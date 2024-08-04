package com.oitsjustjose.vtweaks.common.tweaks.entity.culling;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class EntityCullingRule {
    private final List<ResourceLocation> biomes;
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
                    var location = ResourceLocation.parse(x.substring(1));
                    var tagKey = TagKey.create(Registries.BIOME, location);
                    this.biomeTags.add(tagKey);
                } else {
                    this.biomes.add(ResourceLocation.parse(x));
                }
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in biomes object {}: {}", x, e);
            }
        });

        dimFilterRaw.forEach(x -> {
            try {
                var location = ResourceLocation.parse(x);
                this.dimensions.add(location);
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in dimensions object {}: {}", x, e);
            }
        });

        entityFilterRaw.forEach(x -> {
            try {
                // Case that you've given me a tag
                if (x.startsWith("#")) {
                    var location = ResourceLocation.parse(x.substring(1));
                    var tagKey = TagKey.create(Registries.ENTITY_TYPE, location);
                    this.entityTypeTags.add(tagKey);
                } else {
                    var location = ResourceLocation.parse(x);
                    if (BuiltInRegistries.ENTITY_TYPE.containsKey(location)) {
                        this.entityTypes.add(location);
                    } else {
                        VTweaks.getInstance().LOGGER.warn("Entity type {} was not found in the ENTITY_TYPE registry", x);
                    }
                }
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in entities object {}: {}", x, e);
            }
        });
    }

    public boolean apply(FinalizeSpawnEvent evt) {
        // filter by entity
        var hasMatchedOnEntity = this.entityTypes.contains(BuiltInRegistries.ENTITY_TYPE.getKey(evt.getEntity().getType()));
        var hasMatchedOnEntityType = this.entityTypeTags.stream().anyMatch(typeTag -> evt.getEntity().getType().is(typeTag));
        if (!hasMatchedOnEntity && !hasMatchedOnEntityType) return false;

        var lvl = (ServerLevel) evt.getLevel();
        var dimName = lvl.dimension().location();
        var biomeHolder = lvl.getBiome(evt.getEntity().blockPosition());
        var unwrappedBiome = biomeHolder.unwrap();
        var biomeRegistry = lvl.registryAccess().registry(Registries.BIOME).orElseThrow();

        var hasMatchedOnDim = dimensions.isEmpty() || dimensions.contains(dimName);
        var hasMatchedOnBiomeTag = biomeTags.isEmpty() || biomeTags.stream().anyMatch(biomeHolder::is);
        var hasMatchedOnBiome = biomes.isEmpty() || biomes.stream().anyMatch(b -> {
            var eBiome = unwrappedBiome.right().orElse(null);
            if (eBiome != null) return this.biomes.contains(eBiome);

            var rb = unwrappedBiome.left().orElse(null);
            if (rb != null) {
                eBiome = biomeRegistry.get(rb.location());
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
