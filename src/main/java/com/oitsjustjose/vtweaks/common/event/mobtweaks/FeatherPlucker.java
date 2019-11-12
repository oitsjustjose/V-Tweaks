package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//Idea stolen from copygirl's old tweaks mod. Code is all original, but I liked this

public class FeatherPlucker
{
    @SubscribeEvent
    public void registerEvent(EntityInteract event)
    {
        // Checks if feature is enabled
        if (!MobTweakConfig.ENABLE_FEATHER_PLUCKING.get())
        {
            return;
        }

        if (event.getTarget() == null || !(event.getTarget() instanceof ChickenEntity))
        {
            return;
        }

        ChickenEntity chicken = (ChickenEntity) event.getTarget();

        if (!(event.getEntityLiving() instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if (!chicken.isChild())
        {
            if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof ShearsItem)
            {
                if (!player.world.isRemote && chicken.getGrowingAge() == 0)
                {
                    player.world.addEntity(Utils.createItemEntity(player.world,
                            event.getTarget().getPosition(), Items.FEATHER));
                    chicken.attackEntityFrom(DamageSource.GENERIC, 0.0F);
                    chicken.setGrowingAge(10000); // Used for a cooldown timer, essentially
                    if (!player.isCreative())
                    {
                        player.getHeldItemMainhand().attemptDamageItem(1, player.getRNG(), null);
                    }
                }
                player.swingArm(Hand.MAIN_HAND);
            }
        }
    }
}
