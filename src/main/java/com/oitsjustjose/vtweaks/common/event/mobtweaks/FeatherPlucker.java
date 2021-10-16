package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Utils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//Idea stolen from copygirl's old tweaks mod. Code is all original, but I liked this

public class FeatherPlucker {
    @SubscribeEvent
    public void registerEvent(EntityInteract event) {
        // Checks if feature is enabled
        if (!MobTweakConfig.ENABLE_FEATHER_PLUCKING.get()) {
            return;
        }

        if (event.getTarget() == null || !(event.getTarget() instanceof Chicken)) {
            return;
        }

        Chicken chicken = (Chicken) event.getTarget();

        if (!(event.getEntityLiving() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntityLiving();
        if (!chicken.isBaby()) {
            if (!player.getMainHandItem().isEmpty()
                    && player.getMainHandItem().getItem() instanceof ShearsItem) {
                if (!player.level.isClientSide() && chicken.getAge() == 0) {
                    player.level.addFreshEntity(
                            Utils.createItemEntity(player.level, event.getTarget().getOnPos(), Items.FEATHER));
                    chicken.hurt(DamageSource.GENERIC, 0.0F);
                    chicken.setAge(10000); // Used for a cooldown timer, essentially
                    if (!player.isCreative()) {
                        player.getMainHandItem().hurt(1, player.getRandom(), null);
                    }
                }
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
