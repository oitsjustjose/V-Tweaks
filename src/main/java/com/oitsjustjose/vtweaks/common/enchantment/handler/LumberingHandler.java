package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LumberingHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void register(BlockEvent.BreakEvent event) {
        // Check if enchantment is disabled
        if (!EnchantmentConfig.ENABLE_LUMBERING.get()) {
            return;
        }
        // Check that state, world, player or player's held item all exist
        if (event.getState() == null || event.getLevel() == null || event.getPlayer() == null
                || event.getPlayer().getMainHandItem().isEmpty() || event.getPlayer().isCreative()
                || !(event.getLevel() instanceof ServerLevel)) {
            return;
        }
        ServerLevel world = (ServerLevel) event.getLevel();
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        // Checks if the axe has lumbering
        if (stack.getEnchantmentLevel(VTweaks.Enchantments.lumbering) > 0) {
            if (player.isCrouching()) {
                if (event.getState().is(BlockTags.LOGS)) {
                    chopTree(world, player, event.getPos(), event.getState(), 0);
                }
            }
        }
    }

    private boolean chopTree(ServerLevel world, Player player, BlockPos pos, BlockState original, int curr) {
        if (curr >= EnchantmentConfig.LUMBERING_MAX_INT.get()) {
            if (curr > EnchantmentConfig.LUMBERING_MAX_INT.get()) {
                VTweaks.getInstance().LOGGER.info("Somehow exceeded max lumbering count with value {} (max is {})",
                        curr, EnchantmentConfig.LUMBERING_MAX_INT.get());
            }
            return false;
        }
        for (int mod_x = -1; mod_x <= 1; mod_x++) {
            for (int mod_y = -1; mod_y <= 1; mod_y++) {
                for (int mod_z = -1; mod_z <= 1; mod_z++) {
                    BlockPos iterPos = pos.offset(mod_x, mod_y, mod_z);
                    if (iterPos != pos) {
                        if (!canStillChop(player)) {
                            return false;
                        }
                        if (woodMatchFilter(original, world.getBlockState(iterPos))) {
                            emulateBreak(world, iterPos, player, true);
                            if (!chopTree(world, player, iterPos, original, curr + 1)) {
                                return false;
                            }
                        } else if (EnchantmentConfig.LUMBERING_CUTS_LEAVES.get()) {
                            if (world.getBlockState(iterPos).is(BlockTags.LEAVES)) {
                                emulateBreak(world, iterPos, player, false);
                                if (!chopTree(world, player, iterPos, original, curr + 1)) {
                                    return false;
                                }
                            }
                        }

                    }
                }
            }
        }

        return true;
    }

    private boolean canStillChop(Player player) {
        if (!player.getMainHandItem().canPerformAction(ToolActions.AXE_DIG)) {
            return false;
        }

        ItemStack axe = player.getMainHandItem().copy();
        if (axe.hasTag()) {
            CompoundTag comp = axe.getTag();
            assert comp != null;
            if (comp.contains("Energy")) {
                return comp.getInt("Energy") > 0;
            }
        }

        IEnergyStorage cap = axe.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);
        if (cap != null) {
            return cap.getEnergyStored() > 0;
        }

        return (axe.getMaxDamage() - axe.getDamageValue()) > 2;
    }

    private boolean woodMatchFilter(BlockState original, BlockState current) {
        if (EnchantmentConfig.LUMBERING_WOOD_STRICT.get()) {
            return original.getBlock().getDescriptionId().equals(current.getBlock().getDescriptionId());
        }
        return current.is(BlockTags.LOGS);
    }

    private void emulateBreak(ServerLevel world, BlockPos pos, Player player, boolean damageTool) {
        BlockState state = world.getBlockState(pos);

        state.onRemove(world, pos, state, false);
        Block.dropResources(state, world, pos, world.getBlockEntity(pos));
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        if (damageTool) {
            player.getMainHandItem().hurt(1, player.getRandom(), null);
        }
    }
}