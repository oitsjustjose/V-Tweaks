package com.oitsjustjose.vtweaks.common.entity.culling;

import com.mojang.datafixers.util.Either;
import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
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
                    ResourceLocation rl = new ResourceLocation(x.substring(1));
                    TagKey<Biome> tagKey = TagKey.create(Registry.BIOME_REGISTRY, rl);
                    this.biomeTags.add(tagKey);
                } else {
                    ResourceLocation rl = new ResourceLocation(x);
                    Biome b = ForgeRegistries.BIOMES.getValue(rl);
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
                ResourceLocation rl = new ResourceLocation(x);
                this.dimensions.add(rl);
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in dimensions object {}: {}", x, e);
            }
        });

        entityFilterRaw.forEach(x -> {
            try {
                // Case that you've given me a tag
                if (x.startsWith("#")) {
                    ResourceLocation rl = new ResourceLocation(x.substring(1));
                    TagKey<EntityType<?>> tagKey = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, rl);
                    this.entityTypeTags.add(tagKey);
                } else {
                    ResourceLocation rl = new ResourceLocation(x);
                    EntityType<?> ent = ForgeRegistries.ENTITY_TYPES.getValue(rl);
                    if (ent != null) {
                        this.entityTypes.add(rl);
                    }
                }
            } catch (Exception e) {
                VTweaks.getInstance().LOGGER.error("Error processing culled_entity in entities object {}: {}", x, e);
            }
        });
    }

    public boolean apply(LivingSpawnEvent.CheckSpawn evt) {
        // filter by entity
        boolean hasMatchedOnEntity = this.entityTypes.contains(ForgeRegistries.ENTITY_TYPES.getKey(evt.getEntity().getType()));
        boolean hasMatchedOnEntityType = this.entityTypeTags.stream().anyMatch(typeTag -> evt.getEntity().getType().is(typeTag));
        if (!hasMatchedOnEntity && !hasMatchedOnEntityType) return false;

        ServerLevel lvl = (ServerLevel) evt.getLevel();
        ResourceLocation dimName = lvl.dimension().location();
        Holder<Biome> biomeHolder = lvl.getBiome(evt.getEntity().blockPosition());
        Either<ResourceKey<Biome>, Biome> unwrappedBiome = biomeHolder.unwrap();

        boolean hasMatchedOnDim = dimensions.isEmpty() || dimensions.contains(dimName);
        boolean hasMatchedOnBiomeTag = biomeTags.isEmpty() || biomeTags.stream().anyMatch(biomeHolder::is);
        boolean hasMatchedOnBiome = biomes.isEmpty() || biomes.stream().anyMatch(b -> {
            Biome eBiome = unwrappedBiome.right().orElse(null);
            if (eBiome != null) return this.biomes.contains(eBiome);

            ResourceKey<Biome> rb = unwrappedBiome.left().orElse(null);
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
