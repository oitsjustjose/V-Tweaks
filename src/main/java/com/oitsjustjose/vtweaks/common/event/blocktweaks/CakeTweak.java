package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CakeTweak {
    @SubscribeEvent
    public void registerTweak(BlockEvent.BreakEvent event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_CAKE_DROP.get()) return;
        if (event.getPlayer() == null) return;

        BlockState state = event.getState();
        Block block = state.getBlock();

        if (event.getState().is(BlockTags.CANDLE_CAKES)) {
            ItemEntity cakeItem = new ItemEntity((Level) event.getLevel(),
                    (double) event.getPos().getX() + 0.5D, event.getPos().getY(),
                    (double) event.getPos().getZ() + 0.5D, new ItemStack(Items.CAKE));
            cakeItem.setPickUpDelay(10);
            event.getLevel().addFreshEntity(cakeItem);
        }

        if (event.getState().hasProperty(BlockStateProperties.BITES)) {
            int bites = event.getState().getValue(BlockStateProperties.BITES);
            if (bites == 0) {
                ItemEntity cakeItem = new ItemEntity((Level) event.getLevel(),
                        (double) event.getPos().getX() + 0.5D, event.getPos().getY(),
                        (double) event.getPos().getZ() + 0.5D, new ItemStack(block.asItem()));
                cakeItem.setPickUpDelay(10);
                event.getLevel().addFreshEntity(cakeItem);
            }
        }
    }
}