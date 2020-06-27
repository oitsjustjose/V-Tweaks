package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
            if (event.getState().has(CakeBlock.BITES)) {
                int bites = event.getState().get(CakeBlock.BITES);
                VTweaks.getInstance().LOGGER.info("Cake has " + bites + " bites");
                if (bites == 0) {
                    ItemEntity cakeItem = new ItemEntity(event.getWorld().getWorld(),
                            (double) event.getPos().getX() + 0.5D, (double) event.getPos().getY(),
                            (double) event.getPos().getZ() + 0.5D, new ItemStack(Items.CAKE));
                    cakeItem.setPickupDelay(10);
                    event.getWorld().addEntity(cakeItem);
                }
            }
        }
    }
}