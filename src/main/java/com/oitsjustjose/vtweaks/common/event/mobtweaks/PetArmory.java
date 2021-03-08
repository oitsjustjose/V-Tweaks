package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType.Group;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class PetArmory {
    private static final String TAG = "vtweaks:lootPickupBackup";

    private void backupPriorSettings(TameableEntity ent) {
        CompoundNBT comp = ent.getPersistentData();
        if (!comp.contains(TAG)) {
            comp.putBoolean(TAG, ent.canPickUpLoot());
        }
    }

    private void restorePriorSettings(TameableEntity ent) {
        CompoundNBT comp = ent.getPersistentData();
        if (comp.contains(TAG)) {
            ent.setCanPickUpLoot(comp.getBoolean(TAG));
            comp.remove(TAG);
        }
    }


    /**
     * Allows newly tamed pets to pick up loot and drop it should they *gasp* die.
     */
    @SubscribeEvent
    public void registerEvent(AnimalTameEvent event) {
        if (MobTweakConfig.ENABLE_PET_ARMORY.get()) {
            if (event.getAnimal() instanceof TameableEntity) {
                TameableEntity pet = (TameableEntity) event.getAnimal();
                backupPriorSettings(pet);
                for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                    pet.setDropChance(slotType, 1F);
                }
                pet.setCanPickUpLoot(true);
            }
        }
    }

    /**
     * Makes sure that existing pets are allowed to pick up things
     * if they weren't already
     */
    @SubscribeEvent
    public void registerEvent(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof TameableEntity)) {
            return;
        }

        TameableEntity pet = (TameableEntity) event.getEntity();

        if (MobTweakConfig.ENABLE_PET_ARMORY.get()) {
            if (pet.isTamed()) {
                 for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                    pet.setDropChance(slotType, 1F);
                }
                pet.setCanPickUpLoot(true);
            }
        } else {
            restorePriorSettings(pet);
        }
    }

    @SubscribeEvent
    public void registerEvent(EntityInteract event) {
        // Checks that the feature is enabled
        if (!MobTweakConfig.ENABLE_PET_ARMORY.get()) {
            return;
        }
        // Confirms there's a target
        if (event.getTarget() == null) {
            return;
        }
        // Conirms the target is tamable!
        if (event.getTarget() instanceof TameableEntity) {
            TameableEntity pet = (TameableEntity) event.getTarget();
            if (!(event.getEntityLiving() instanceof PlayerEntity)) {
                return;
            }

            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            boolean strippedArmor = false;

            if (player.getHeldItemMainhand().isEmpty() && player.isCrouching() && pet.isTamed()
                    && pet.isOwner(player)) {
                // Give the armor back
                for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                    ItemStack itemInSlot = pet.getItemStackFromSlot(slotType);

                    if (!itemInSlot.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, itemInSlot.copy());
                        pet.setItemStackToSlot(slotType, ItemStack.EMPTY);
                        strippedArmor = true;
                    }
                }
                if (strippedArmor) {
                    player.playSound(SoundEvents.ENTITY_HORSE_ARMOR, .5F, 0.85F);
                }
            }
        }
    }
}