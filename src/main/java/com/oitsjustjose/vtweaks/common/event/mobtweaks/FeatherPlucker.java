package com.oitsjustjose.vtweaks.common.event.mobtweaks;

import com.oitsjustjose.vtweaks.common.config.CommonConfig;
import com.oitsjustjose.vtweaks.common.config.MobTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.Utils;

import net.minecraft.nbt.CompoundTag;
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
    private static final String PLUCK_COOLDOWN_KEY = Constants.MODID + ":pluck_cooldown";

    @SubscribeEvent
    public void registerEvent(EntityInteract event) {
        // Checks if feature is enabled
        if (!MobTweakConfig.ENABLE_FEATHER_PLUCKING.get()) return;

        if (event.getTarget() == null) return;
        if (!(event.getTarget() instanceof Chicken chicken)) return;
        if (event.getEntity() == null) return;

        Player player = (Player) event.getEntity();

        if (!player.getMainHandItem().isEmpty()
                && player.getMainHandItem().getItem() instanceof ShearsItem) {
            if (!player.level.isClientSide() && canPluck(chicken)) {
                player.level.addFreshEntity(
                        Utils.createItemEntity(player.level, event.getTarget().getOnPos(),
                                Items.FEATHER));
                chicken.hurt(DamageSource.GENERIC, 0.0F);
                setCooldown(chicken);
                if (!player.isCreative()) {
                    player.getMainHandItem().hurt(1, player.getRandom(), null);
                }
            }
            player.swing(InteractionHand.MAIN_HAND);
        }
    }

    private boolean canPluck(Chicken chicken) {
        CompoundTag tag = chicken.getPersistentData();
        // No pluck cooldown key, means never plucked..
        if (!tag.contains(PLUCK_COOLDOWN_KEY)) return true;
        long lastTime = tag.getLong(PLUCK_COOLDOWN_KEY);
        return System.currentTimeMillis() - lastTime >= MobTweakConfig.FEATHER_PLUCKING_COOLDOWN.get();
    }

    private void setCooldown(Chicken chicken) {
        CompoundTag tag = chicken.getPersistentData();
        tag.putLong(PLUCK_COOLDOWN_KEY, System.currentTimeMillis());
    }
}
