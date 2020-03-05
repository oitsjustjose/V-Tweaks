package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class NaturalBabies
{

    @SubscribeEvent
    public void registerEvent(LivingSpawnEvent event)
    {

        if (MobTweakConfig.NATURAL_BABIES_CHANCE.get() == 0)
        {
            return;
        }

        if (event.getEntityLiving() instanceof AnimalEntity)
        {
            AnimalEntity animal = (AnimalEntity) event.getEntityLiving();

            // Don't bother if the entity is already a babbi,
            // Also skip if we've already checked it
            if (animal.isChild() || animal.getPersistentData().getBoolean("vtweaks:naturalBaby"))
            {
                return;
            }

            animal.getPersistentData().putBoolean("vtweaks:naturalBaby", true);

            if (animal.getRNG().nextInt(100) <= MobTweakConfig.NATURAL_BABIES_CHANCE.get())
            {
                animal.setGrowingAge(-1000);
            }
        }
    }

}