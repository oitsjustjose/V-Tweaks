package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.util.HelperFunctions;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum ChallengerMobType
{
    MIGHTY(0.18D, 80F, "Mighty", new int[]
    { 3, 236, 252 }), HUNGRY(0.25D, 40F, "Hungry", new int[]
    { 163, 127, 77 }), RANGER(0.25D, 40F, "Ranger", new int[]
    { 81, 163, 77 }), ARCANE(0.25D, 40F, "Arcane", new int[]
    { 63, 10, 92 }), PYRO(0.25D, 40F, "Pyro", new int[]
    { 196, 41, 6 }), ZESTONIAN(0.25D, 40F, "Zestonian", new int[]
    { 155, 6, 196 }), REINFORCED(0.25D, 160F, "Reinforced", new int[]
    { 64, 1, 7 }), AGILE(0.6D, 10F, "Agile", new int[]
    { 10, 209, 169 });

    private final double mobSpeed;
    private final float mobHealth;
    private final ItemStack heldItem;
    private final String commonName;
    private final int[] colors;

    ChallengerMobType(double speed, float health, String name, int[] colors)
    {
        this.mobSpeed = speed;
        this.mobHealth = health;
        this.heldItem = toolForMobClass(this.ordinal());
        this.commonName = name;
        this.colors = colors;
    }

    ItemStack toolForMobClass(int type)
    {
        ItemStack[] toolList = new ItemStack[]
        { new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.BOWL), new ItemStack(Items.BOW),
                new ItemStack(Items.STICK), new ItemStack(Items.FIREWORK_ROCKET), new ItemStack(Items.OAK_SIGN),
                ItemStack.EMPTY, ItemStack.EMPTY };

        toolList[0].setDamage(toolList[0].getMaxDamage() / 8);
        toolList[0].addEnchantment(HelperFunctions.getEnchantment("minecraft", "sharpness"), 3);
        toolList[1].addEnchantment(HelperFunctions.getEnchantment("minecraft", "sharpness"), 10);
        toolList[2].addEnchantment(HelperFunctions.getEnchantment("minecraft", "punch"), 2);
        toolList[2].addEnchantment(HelperFunctions.getEnchantment("minecraft", "power"), 3);
        toolList[3].addEnchantment(HelperFunctions.getEnchantment("minecraft", "fire_aspect"), 1);
        toolList[3].addEnchantment(HelperFunctions.getEnchantment("minecraft", "knockback"), 2);
        toolList[4].addEnchantment(HelperFunctions.getEnchantment("minecraft", "fire_aspect"), 5);
        toolList[5].addEnchantment(HelperFunctions.getEnchantment("minecraft", "knockback"), 10);

        return toolList[type];
    }

    public double getSpeed()
    {
        return this.mobSpeed;
    }

    public float getHealth()
    {
        return this.mobHealth;
    }

    public ItemStack getEquipment()
    {
        return this.heldItem;
    }

    public String getPrefix()
    {
        return this.commonName;
    }

    public float getRed()
    {
        return ((float) this.colors[0] / 255);
    }

    public float getGreen()
    {
        return ((float) this.colors[1] / 255);
    }

    public float getBlue()
    {
        return ((float) this.colors[2] / 255);
    }
}