package com.oitsjustjose.vtweaks.common.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChallengerMob {

    Vector3f particleColor;
    private final int weight;
    private final String unlocalizedName;

    private final double healthMultiplier;
    private final double speedMultiplier;

    private final ItemStack mainHand;
    private final ItemStack offHand;
    private final ItemStack helmet;
    private final ItemStack chestplate;
    private final ItemStack leggings;
    private final ItemStack boots;

    private final List<MobEffectInstance> effectsOnAttack;
    private final WeightedCollection<ItemStack> loot;

    private final List<ResourceLocation> entityFilter;
    private final boolean entityFilterIsBlacklist;

    public ChallengerMob(JsonObject json) {
        if (json == null) {
            throw new NullPointerException("Provided JSON is null");
        }

        this.weight = json.get("weight").getAsInt();

        JsonObject color = json.get("color").getAsJsonObject();
        float r = ((float) color.get("red").getAsInt()) / 255F;
        float g = ((float) color.get("green").getAsInt()) / 255F;
        float b = ((float) color.get("blue").getAsInt()) / 255F;
        this.particleColor = new Vector3f(r, g, b);

        this.unlocalizedName = json.get("unlocalizedName").getAsString();

        this.healthMultiplier = json.get("healthMultiplier").getAsDouble();
        this.speedMultiplier = json.get("speedMultiplier").getAsDouble();

        JsonObject gear = json.get("gear").getAsJsonObject();
        this.mainHand = ChallengerMobSer.deserializeItemStack(gear, "mainHand");
        this.offHand = ChallengerMobSer.deserializeItemStack(gear, "off Hand");
        this.helmet = ChallengerMobSer.deserializeItemStack(gear, "helmet");
        this.chestplate = ChallengerMobSer.deserializeItemStack(gear, "chestplate");
        this.leggings = ChallengerMobSer.deserializeItemStack(gear, "leggings");
        this.boots = ChallengerMobSer.deserializeItemStack(gear, "boots");

        this.effectsOnAttack = ChallengerMobSer.deserializeEffectInstanceList(json.get("effectsOnAttack").getAsJsonArray());
        this.loot = ChallengerMobSer.deserializeLootTable(json.get("loot").getAsJsonArray());

        JsonObject entityFilter = json.get("entityFilter").getAsJsonObject();
        this.entityFilterIsBlacklist = entityFilter.get("isBlacklist").getAsBoolean();
        this.entityFilter = new ArrayList<>();
        for (JsonElement e : entityFilter.get("entities").getAsJsonArray()) {
            this.entityFilter.add(new ResourceLocation(e.getAsString()));
        }
    }

    public void apply(Monster monster) {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            monster.setItemSlot(e, ItemStack.EMPTY);
            monster.setDropChance(e, 0.0F);
        }

        monster.setItemSlot(EquipmentSlot.HEAD, this.helmet);
        monster.setItemSlot(EquipmentSlot.CHEST, this.chestplate);
        monster.setItemSlot(EquipmentSlot.LEGS, this.leggings);
        monster.setItemSlot(EquipmentSlot.FEET, this.boots);
        monster.setItemInHand(InteractionHand.MAIN_HAND, this.mainHand);
        monster.setItemInHand(InteractionHand.OFF_HAND, this.offHand);

        double speed = monster.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
        double health = monster.getAttribute(Attributes.MAX_HEALTH).getBaseValue();

        monster.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * this.speedMultiplier);
        monster.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health * this.healthMultiplier);
        monster.setHealth((float) (health * this.healthMultiplier));

        monster.setCustomName(mobClassName(monster));

        CompoundTag comp = monster.getPersistentData();
        CompoundTag type = new CompoundTag();
        type.putString("variant", this.unlocalizedName);
        comp.put("challenger_mob_data", type);

        // Infinite Fire Resistance
        monster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
    }

    private TranslatableComponent mobClassName(Monster mob) {
        return new TranslatableComponent("vtweaks." + this.unlocalizedName + ".challenger.mob",
                mob.getName());
    }

    @Nullable
    public ItemStack pickLoot() {
        return this.loot.pick();
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public Vector3f getParticleColor() {
        return this.particleColor;
    }

    public int getWeight() {
        return this.weight;
    }

    public List<MobEffectInstance> getAttackEffects() {
        return this.effectsOnAttack;
    }

    public List<ResourceLocation> getEntityFilter() {
        return this.entityFilter;
    }

    public boolean isEntityFilterIsBlacklist() {
        return this.entityFilterIsBlacklist;
    }
}
