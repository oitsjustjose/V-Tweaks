package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CakeTweak {
    @SubscribeEvent
    public void registerTweak(BlockEvent.BreakEvent event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_CAKE_DROP.get()) {
            return;
        }

        Block block = event.getState().getBlock();
        if (event.getPlayer() != null && block instanceof CakeBlock) {
            if (event.getState().hasProperty(CakeBlock.BITES)) {
                int bites = event.getState().getValue(CakeBlock.BITES);
                if (bites == 0) {
                    ItemEntity cakeItem = new ItemEntity((Level) event.getWorld(),
                            (double) event.getPos().getX() + 0.5D, event.getPos().getY(),
                            (double) event.getPos().getZ() + 0.5D, new ItemStack(Items.CAKE));
                    cakeItem.setPickUpDelay(10);
                    event.getWorld().addFreshEntity(cakeItem);
                }
            }
        }
    }
}