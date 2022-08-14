package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class PetArmory {
    private static final String TAG = "vtweaks:lootPickupBackup";

    private void backupPriorSettings(TamableAnimal ent) {
        CompoundTag comp = ent.getPersistentData();
        if (!comp.contains(TAG)) {
            comp.putBoolean(TAG, ent.canPickUpLoot());
        }
    }

    private void restorePriorSettings(TamableAnimal ent) {
        CompoundTag comp = ent.getPersistentData();
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
            if (event.getAnimal() instanceof TamableAnimal) {
                TamableAnimal pet = (TamableAnimal) event.getAnimal();
                backupPriorSettings(pet);
                for (EquipmentSlot slotType : EquipmentSlot.values()) {
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
    public void registerEvent(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof TamableAnimal pet)) return;

        if (MobTweakConfig.ENABLE_PET_ARMORY.get()) {
            if (pet.isTame()) {
                for (EquipmentSlot slotType : EquipmentSlot.values()) {
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
        if (!MobTweakConfig.ENABLE_PET_ARMORY.get()) return;
        if (event.getTarget() == null) return;

        // Conirms the target is tamable!
        if (event.getTarget() instanceof TamableAnimal pet) {
            Player player = event.getEntity();
            boolean strippedArmor = false;

            if (player.getMainHandItem().isEmpty() && player.isCrouching() && pet.isTame()
                    && pet.isOwnedBy(player)) {
                // Give the armor back
                for (EquipmentSlot slotType : EquipmentSlot.values()) {
                    ItemStack itemInSlot = pet.getItemBySlot(slotType);

                    if (!itemInSlot.isEmpty()) {
                        ItemHandlerHelper.giveItemToPlayer(player, itemInSlot.copy());
                        pet.setItemSlot(slotType, ItemStack.EMPTY);
                        strippedArmor = true;
                    }
                }
                if (strippedArmor) {
                    player.playSound(SoundEvents.HORSE_ARMOR, .5F, 0.85F);
                }
            }
        }
    }
}