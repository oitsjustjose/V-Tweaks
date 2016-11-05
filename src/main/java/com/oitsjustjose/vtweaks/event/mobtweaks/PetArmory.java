package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PetArmory
{
	@SubscribeEvent
	public void registerEvent(EntityInteract event)
	{
		if (event.getTarget() == null)
			return;

		if (event.getTarget() instanceof EntityTameable)
		{
			EntityTameable tameable = (EntityTameable) event.getTarget();
			EntityPlayer player = event.getEntityPlayer();

			if (player.getHeldItemMainhand() != null && tameable.isTamed())
			{
				if (player.getHeldItemMainhand().getItem() == Items.IRON_HORSE_ARMOR)
				{
					tameable.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
					tameable.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));

					--player.getHeldItemMainhand().stackSize;
				}
				else if (player.getHeldItemMainhand().getItem() == Items.GOLDEN_HORSE_ARMOR)
				{
					tameable.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
					tameable.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));

					--player.getHeldItemMainhand().stackSize;
				}
				else if (player.getHeldItemMainhand().getItem() == Items.DIAMOND_HORSE_ARMOR)
				{
					tameable.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
					tameable.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
					tameable.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
					
					--player.getHeldItemMainhand().stackSize;
				}
			}
		}
	}
}
