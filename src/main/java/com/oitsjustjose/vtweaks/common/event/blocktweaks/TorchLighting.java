package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;
import com.oitsjustjose.vtweaks.common.util.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TorchLighting {
    @SubscribeEvent
    public void registerEvent(RightClickBlock event) {
        if (!BlockTweakConfig.ENABLE_TORCH_LIGHT_TWEAKS.get()) {
            return;
        }

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Level level = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getPlayer();

        if (player.isCrouching()) {
            return;
        }

        // Case that it's not ignitable
        if (!state.hasProperty(BlockStateProperties.LIT)) {
            return;
        }

        // Case that the player's main-hand item isn't a torch item
        if (!player.getItemInHand(InteractionHand.MAIN_HAND).is(Constants.IGNITION)) {
            return;
        }

        if (state.getValue(BlockStateProperties.LIT) == Boolean.FALSE) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 3);
            level.playSound(null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                    SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.15F, 1.0F);
        }

        player.swing(InteractionHand.MAIN_HAND);
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }
}
