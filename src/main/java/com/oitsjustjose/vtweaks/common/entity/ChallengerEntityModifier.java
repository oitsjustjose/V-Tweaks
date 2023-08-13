package com.oitsjustjose.vtweaks.common.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.common.data.helpers.VTJsonHelpers;
import com.oitsjustjose.vtweaks.common.util.WeightedCollection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChallengerEntityModifier {
    public static final WeightedCollection<ChallengerEntityModifier> AllVariants = new WeightedCollection<>();
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
    Vector3f particleColor;

    public ChallengerEntityModifier(JsonObject json) {
        if (json == null) {
            throw new NullPointerException("Provided JSON is null");
        }

        this.weight = json.get("weight").getAsInt();

        var color = json.get("color").getAsJsonObject();
        var r = ((float) color.get("red").getAsInt()) / 255F;
        var g = ((float) color.get("green").getAsInt()) / 255F;
        var b = ((float) color.get("blue").getAsInt()) / 255F;
        this.particleColor = new Vector3f(r, g, b);

        this.unlocalizedName = json.get("unlocalizedName").getAsString();

        this.healthMultiplier = json.get("healthMultiplier").getAsDouble();
        this.speedMultiplier = json.get("speedMultiplier").getAsDouble();

        JsonObject gear = json.get("gear").getAsJsonObject();
        this.mainHand = VTJsonHelpers.deserializeItemStack(gear, "mainHand");
        this.offHand = VTJsonHelpers.deserializeItemStack(gear, "offHand");
        this.helmet = VTJsonHelpers.deserializeItemStack(gear, "helmet");
        this.chestplate = VTJsonHelpers.deserializeItemStack(gear, "chestplate");
        this.leggings = VTJsonHelpers.deserializeItemStack(gear, "leggings");
        this.boots = VTJsonHelpers.deserializeItemStack(gear, "boots");

        this.effectsOnAttack = VTJsonHelpers.deserializeEffectInstanceList(json.get("effectsOnAttack").getAsJsonArray());
        this.loot = VTJsonHelpers.deserializeLootTable(json.get("loot").getAsJsonArray());

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

        monster.setItemSlot(EquipmentSlot.HEAD, this.helmet.copy());
        monster.setItemSlot(EquipmentSlot.CHEST, this.chestplate.copy());
        monster.setItemSlot(EquipmentSlot.LEGS, this.leggings.copy());
        monster.setItemSlot(EquipmentSlot.FEET, this.boots.copy());
        monster.setItemInHand(InteractionHand.MAIN_HAND, this.mainHand.copy());
        monster.setItemInHand(InteractionHand.OFF_HAND, this.offHand.copy());

        double speed = Objects.requireNonNull(monster.getAttribute(Attributes.MOVEMENT_SPEED)).getBaseValue();
        double health = Objects.requireNonNull(monster.getAttribute(Attributes.MAX_HEALTH)).getBaseValue();

        Objects.requireNonNull(monster.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(speed * this.speedMultiplier);
        Objects.requireNonNull(monster.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(health * this.healthMultiplier);
        monster.setHealth((float) (health * this.healthMultiplier));

        monster.setCustomName(mobClassName(monster));

        CompoundTag comp = monster.getPersistentData();
        CompoundTag type = new CompoundTag();
        type.putString("variant", this.unlocalizedName);
        comp.put("challenger_mob_data", type);

        // Infinite Fire Resistance
        monster.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
    }

    private MutableComponent mobClassName(Monster mob) {
        TranslatableContents c = new TranslatableContents(
                "vtweaks." + this.unlocalizedName + ".challenger.mob",
                "Challenger Mob",
                new Object[]{mob.getName()}
        );
        try {
            return c.resolve(null, mob, 0);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return Component.empty().append(e.getMessage());
        }
    }

    @Nullable
    public ItemStack pickLoot() {
        var loot = this.loot.pick();
        return loot == null ? null : loot.copy();
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
