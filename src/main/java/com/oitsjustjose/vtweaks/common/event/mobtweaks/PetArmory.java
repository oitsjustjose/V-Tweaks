package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PetArmory
{
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

            if (!player.getHeldItemMainhand().isEmpty() && pet.isTamed() && pet.isOwner(player))
            {
                if (player.getHeldItemMainhand().getItem() == Items.IRON_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                    pet.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                    pet.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                    pet.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
                else if (player.getHeldItemMainhand().getItem() == Items.GOLDEN_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.GOLDEN_BOOTS));
                    pet.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                    pet.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                    pet.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
                else if (player.getHeldItemMainhand().getItem() == Items.DIAMOND_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                    pet.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                    pet.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                    pet.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.DIAMOND_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
            }
        }
    }
}