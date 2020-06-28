package com.oitsjustjose.vtweaks.common.event.playertweaks;

import javax.annotation.Nullable;

import com.oitsjustjose.vtweaks.common.config.PlayerTweakConfig;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * A tweak which makes the player swing their arm when doing certain
 * interactions Adds parity to MC Bedrock Edition, and inspired by Snapshot
 * 19W42A: https://www.minecraft.net/en-us/article/minecraft-snapshot-19w42a
 */

public class ArmSwingParity {
    @SubscribeEvent
    public void onEntityInteract(EntityInteract event) {
        // Checks if feature is enabled
        if (!PlayerTweakConfig.ENABLE_SWING_PARITY.get()) {
            return;
        }
        // Check to see if the target exists (never know)
        if (event.getTarget() == null) {
            return;
        }

        // Sheep shearing
        if (event.getTarget() instanceof SheepEntity) {
            SheepEntity sheep = (SheepEntity) event.getTarget();
            Hand shearHoldingHand = findHand(event.getPlayer(), Items.SHEARS);
            if (!sheep.getSheared() && !sheep.isChild() && shearHoldingHand != null) {
                event.getPlayer().swingArm(shearHoldingHand);
                return;
            }
        }

        // Mooshroom shearing and mushroom-milking
        if (event.getTarget() instanceof MooshroomEntity) {
            MooshroomEntity mooshroom = (MooshroomEntity) event.getTarget();
            if (!mooshroom.isChild()) {
                Hand shearHoldingHand = findHand(event.getPlayer(), Items.SHEARS);
                if (shearHoldingHand != null) {
                    event.getPlayer().swingArm(shearHoldingHand);
                    return;
                }

                Hand bowlHoldingHand = findHandSpecific(event.getPlayer(), Items.BOWL);
                if (bowlHoldingHand != null) {
                    event.getPlayer().swingArm(bowlHoldingHand);
                    return;
                }
            }
        }

        // Cow milking
        if (event.getTarget() instanceof CowEntity) {
            CowEntity cow = (CowEntity) event.getTarget();
            if (!cow.isChild()) {
                Hand bucketHoldingHand = findHandSpecific(event.getPlayer(), Items.BUCKET);
                if (bucketHoldingHand != null) {
                    event.getPlayer().swingArm(bucketHoldingHand);
                    return;
                }
            }
        }

        // Fish catching
        if (event.getTarget() instanceof AbstractFishEntity) {
            AbstractFishEntity fish = (AbstractFishEntity) event.getTarget();
            if (!fish.isChild()) {
                Hand bucketHoldingHand = findHandSpecific(event.getPlayer(), Items.WATER_BUCKET);
                if (bucketHoldingHand != null) {
                    event.getPlayer().swingArm(bucketHoldingHand);
                    return;
                }
            }
        }

        // Horse Taming
        if (event.getEntity() instanceof AbstractHorseEntity) {
            Hand sugarHoldingHand = findHandSpecific(event.getPlayer(), Items.SUGAR);
            if (sugarHoldingHand != null) {
                event.getPlayer().swingArm(sugarHoldingHand);
                return;
            }
        }

        // Breeding an animal with their corresponding food
        if (event.getEntity() instanceof AnimalEntity) {
            AnimalEntity animal = (AnimalEntity) event.getEntity();
            if (animal.isBreedingItem(event.getPlayer().getHeldItemMainhand())) {
                event.getPlayer().swingArm(Hand.MAIN_HAND);
                return;
            }
            if (animal.isBreedingItem(event.getPlayer().getHeldItemOffhand())) {
                event.getPlayer().swingArm(Hand.OFF_HAND);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        // Checks if feature is enabled
        if (!PlayerTweakConfig.ENABLE_SWING_PARITY.get()) {
            return;
        }
        if (event.isMounting()) {
            if (event.getEntityMounting() instanceof PlayerEntity) {

                PlayerEntity player = (PlayerEntity) event.getEntityMounting();
                player.swingArm(Hand.MAIN_HAND);
            }
        }
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.RightClickBlock event) {
        // Checks if feature is enabled
        if (!PlayerTweakConfig.ENABLE_SWING_PARITY.get()) {
            return;
        }

        // Bucket swing on empty/fill
        if (findHand(event.getPlayer(), Items.BUCKET) != null) {
            event.getPlayer().swingArm(findHand(event.getPlayer(), Items.BUCKET));
            return;
        }

        // Placing a boat
        if (findHand(event.getPlayer(), Items.OAK_BOAT) != null) {
            event.getPlayer().swingArm(findHand(event.getPlayer(), Items.OAK_BOAT));
            return;
        }
    }

    @Nullable
    private Hand findHand(PlayerEntity player, Item item) {
        if (item.getClass().isInstance(player.getHeldItemMainhand().getItem())) {
            return Hand.MAIN_HAND;
        } else if (item.getClass().isInstance(player.getHeldItemOffhand().getItem())) {
            return Hand.OFF_HAND;
        }

        return null;
    }

    @Nullable
    private Hand findHandSpecific(PlayerEntity player, Item item) {
        if (player.getHeldItemMainhand().getItem() == item) {
            return Hand.MAIN_HAND;
        } else if (player.getHeldItemOffhand().getItem() == item) {
            return Hand.OFF_HAND;
        }
        return null;
    }
}