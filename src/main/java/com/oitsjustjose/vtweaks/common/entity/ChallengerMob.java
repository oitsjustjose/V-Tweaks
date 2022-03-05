package com.oitsjustjose.vtweaks.common.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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

    private final List<EffectInstance> effectsOnAttack;
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

    public void apply(MonsterEntity monster) {
        for (EquipmentSlotType e : EquipmentSlotType.values()) {
            monster.setItemStackToSlot(e, ItemStack.EMPTY);
            monster.setDropChance(e, 0.0F);
        }

        monster.setItemStackToSlot(EquipmentSlotType.HEAD, this.helmet);
        monster.setItemStackToSlot(EquipmentSlotType.CHEST, this.chestplate);
        monster.setItemStackToSlot(EquipmentSlotType.LEGS, this.leggings);
        monster.setItemStackToSlot(EquipmentSlotType.FEET, this.boots);
        monster.setHeldItem(Hand.MAIN_HAND, this.mainHand);
        monster.setHeldItem(Hand.OFF_HAND, this.offHand);

        double speed = monster.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
        double health = monster.getAttribute(Attributes.MAX_HEALTH).getBaseValue();

        monster.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed * this.speedMultiplier);
        monster.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health * this.healthMultiplier);
        monster.setHealth((float) (health * this.healthMultiplier));

        monster.setCustomName(mobClassName(monster));

        CompoundNBT comp = monster.getPersistentData();
        CompoundNBT type = new CompoundNBT();
        type.putString("variant", this.unlocalizedName);
        comp.put("challenger_mob_data", type);

        // Infinite Fire Resistance
        monster.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
    }

    private ITextComponent mobClassName(MonsterEntity mob) {
        return new TranslationTextComponent("vtweaks." + this.unlocalizedName + ".challenger.mob",
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

    public List<EffectInstance> getAttackEffects() {
        return this.effectsOnAttack;
    }

    public List<ResourceLocation> getEntityFilter() {
        return this.entityFilter;
    }

    public boolean isEntityFilterIsBlacklist() {
        return this.entityFilterIsBlacklist;
    }
}
