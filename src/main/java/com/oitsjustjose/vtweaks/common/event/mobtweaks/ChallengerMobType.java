package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.util.Utils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;

public enum ChallengerMobType {
    MIGHTY(0.18D, 80F, "Mighty", new int[]{3, 236, 252}, null),
    HUNGRY(0.25D, 40F, "Hungry", new int[]{163, 127, 77},
            new MobEffectInstance[]{new MobEffectInstance(MobEffects.HUNGER, 600, 2, false, true)}),
    RANGER(0.25D, 40F, "Ranger", new int[]{81, 163, 77}, null),
    ARCANE(0.25D, 40F, "Arcane", new int[]{63, 10, 92},
            new MobEffectInstance[]{new MobEffectInstance(MobEffects.BLINDNESS, 400, 0, false, true),
                    new MobEffectInstance(MobEffects.WITHER, 100, 1, false, true),
                    new MobEffectInstance(MobEffects.LEVITATION, 100, 0, false, true)}),
    PYRO(0.25D, 40F, "Pyro", new int[]{196, 41, 6}, null),
    ZISTONIAN(0.25D, 40F, "Zistonian", new int[]{155, 6, 196}, null),
    REINFORCED(0.25D, 160F, "Reinforced", new int[]{64, 1, 7}, null),
    AGILE(0.4D, 10F, "Agile", new int[]{10, 209, 169}, null);

    private final double mobSpeed;
    private final float mobHealth;
    private final ItemStack heldItem;
    private final String commonName;
    private final int[] colors;
    private final MobEffectInstance[] potionEffects;

    ChallengerMobType(double speed, float health, String name, int[] colors, @Nullable MobEffectInstance[] potionEffects) {
        this.mobSpeed = speed;
        this.mobHealth = health;
        this.heldItem = toolForMobClass(this.ordinal());
        this.commonName = name;
        this.colors = colors;
        this.potionEffects = potionEffects;
    }

    ItemStack toolForMobClass(int type) {
        ItemStack[] toolList = new ItemStack[]{new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.BOWL),
                new ItemStack(Items.BOW), new ItemStack(Items.STICK), new ItemStack(Items.FLINT_AND_STEEL),
                new ItemStack(Items.OAK_SIGN), ItemStack.EMPTY, ItemStack.EMPTY};

        toolList[0].setDamageValue(toolList[0].getMaxDamage() / 8);
        toolList[0].enchant(Utils.getEnchantment("minecraft", "sharpness"), 3);
        toolList[1].enchant(Utils.getEnchantment("minecraft", "sharpness"), 10);
        toolList[2].enchant(Utils.getEnchantment("minecraft", "punch"), 2);
        toolList[2].enchant(Utils.getEnchantment("minecraft", "power"), 3);
        toolList[3].enchant(Utils.getEnchantment("minecraft", "unbreaking"), 1);
        toolList[4].enchant(Utils.getEnchantment("minecraft", "fire_aspect"), 5);
        toolList[5].enchant(Utils.getEnchantment("minecraft", "knockback"), 10);

        return toolList[type];
    }

    public double getSpeed() {
        return this.mobSpeed;
    }

    public float getHealth() {
        return this.mobHealth;
    }

    public ItemStack getEquipment() {
        return this.heldItem;
    }

    public String getPrefix() {
        return this.commonName;
    }

    public float getRed() {
        return ((float) this.colors[0] / 255);
    }

    public float getGreen() {
        return ((float) this.colors[1] / 255);
    }

    public float getBlue() {
        return ((float) this.colors[2] / 255);
    }

    @Nullable
    public MobEffectInstance[] getHitEffects() {
        return (this.potionEffects == null || this.potionEffects.length == 0) ? null : this.potionEffects;
    }
}