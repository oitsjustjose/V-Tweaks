package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.HelperFunctions;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.EquipmentSlotType.Group;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class PetArmory
{
    @SubscribeEvent
    public void registerEvent(AnimalTameEvent event)
    {
        if (event.getAnimal() instanceof TameableEntity)
        {
            TameableEntity pet = (TameableEntity) event.getAnimal();
            for (EquipmentSlotType slotType : EquipmentSlotType.values())
            {
                pet.setDropChance(slotType, 1F);
            }
            pet.setCanPickUpLoot(true);
        }
    }

    @SubscribeEvent
    public void registerEvent(EntityEvent event)
    {
        if (event.getEntity() == null)
        {
            return;
        }
        if (MobTweakConfig.ENABLE_PET_ARMORY_WEAPONS.get())
        {
            return;
        }
        try
        {
            if (event.getEntity() instanceof TameableEntity)
            {
                TameableEntity pet = (TameableEntity) event.getEntity();

                if (pet.getHeldItemMainhand() != null && !pet.getHeldItemMainhand().isEmpty())
                {
                    ItemStack heldStack = pet.getHeldItemMainhand().copy();
                    ItemEntity stackEntity = HelperFunctions.createItemEntity(pet.getEntityWorld(), pet.getPosition()
                            .add(pet.getLookVec().x * 2, pet.getLookVec().y * 2, pet.getLookVec().z * 2), heldStack);
                    pet.getEntityWorld().addEntity(stackEntity);
                    pet.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                    VTweaks.getInstance().LOGGER.info("Should've set MAIN_HAND empty");
                }

                if (pet.getHeldItemOffhand() != null && !pet.getHeldItemOffhand().isEmpty())
                {
                    ItemStack heldStack = pet.getHeldItemOffhand().copy();
                    ItemEntity stackEntity = HelperFunctions.createItemEntity(pet.getEntityWorld(), pet.getPosition()
                            .add(pet.getLookVec().x * 2, pet.getLookVec().y * 2, pet.getLookVec().z * 2), heldStack);
                    pet.getEntityWorld().addEntity(stackEntity);
                    pet.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);
                    VTweaks.getInstance().LOGGER.info("Should've set OFF_HAND empty");
                }

            }

        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            return;
        }
    }

    @SubscribeEvent
    public void registerEvent(EntityJoinWorldEvent event)
    {
        if (!MobTweakConfig.ENABLE_PET_ARMORY.get())
        {
            return;
        }
        if (event.getEntity() instanceof TameableEntity)
        {
            TameableEntity pet = (TameableEntity) event.getEntity();
            if (pet.isTamed())
            {
                for (EquipmentSlotType slotType : EquipmentSlotType.values())
                {
                    pet.setDropChance(slotType, 1F);
                }
                pet.setCanPickUpLoot(true);
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(EntityInteract event)
    {
        // Checks that the feature is enabled
        if (!MobTweakConfig.ENABLE_PET_ARMORY.get())
        {
            return;
        }
        // Confirms there's a target
        if (event.getTarget() == null)
        {
            return;
        }
        // Conirms the target is tamable!
        if (event.getTarget() instanceof TameableEntity)
        {
            TameableEntity pet = (TameableEntity) event.getTarget();
            if (!(event.getEntityLiving() instanceof PlayerEntity))
            {
                return;
            }

            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            boolean strippedArmor = false;

            if (player.getHeldItemMainhand().isEmpty() && player.isSneaking() && pet.isTamed() && pet.isOwner(player))
            {
                // Give the armor back
                for (EquipmentSlotType slotType : EquipmentSlotType.values())
                {
                    if (slotType.getSlotType() == Group.HAND)
                    {
                        continue;
                    }

                    ItemStack itemInSlot = pet.getItemStackFromSlot(slotType);

                    if (!itemInSlot.isEmpty())
                    {
                        ItemHandlerHelper.giveItemToPlayer(player, itemInSlot.copy());
                        pet.setItemStackToSlot(slotType, ItemStack.EMPTY);
                        strippedArmor = true;
                    }
                }
                if (strippedArmor)
                {
                    player.playSound(SoundEvents.ENTITY_HORSE_ARMOR, .5F, 0.85F);
                }
            }
        }
    }
}