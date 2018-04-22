package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PetArmory
{
    @SubscribeEvent
    public void registerEvent(EntityInteract event)
    {
        // Checks that the feature is enabled
        if (!ModConfig.mobTweaks.enablePetArmory)
        {
            return;
        }
        // Confirms there's a target
        if (event.getTarget() == null)
        {
            return;
        }
        // Conirms the target is tamable!
        if (event.getTarget() instanceof EntityTameable)
        {
            EntityTameable pet = (EntityTameable) event.getTarget();
            EntityPlayer player = event.getEntityPlayer();

            if (!player.getHeldItemMainhand().isEmpty() && pet.isTamed() && pet.isOwner(player))
            {
                if (player.getHeldItemMainhand().getItem() == Items.IRON_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                    pet.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
                else if (player.getHeldItemMainhand().getItem() == Items.GOLDEN_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                    pet.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
                else if (player.getHeldItemMainhand().getItem() == Items.DIAMOND_HORSE_ARMOR)
                {
                    pet.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
                    pet.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
                    pet.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));

                    player.getHeldItemMainhand().shrink(1);
                    player.playSound(new SoundEvent(new ResourceLocation("entity.horse.armor")), 1.0F, 0.85F);
                }
            }
        }
    }
}