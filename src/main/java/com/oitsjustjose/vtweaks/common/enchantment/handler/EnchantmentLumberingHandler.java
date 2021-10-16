package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantmentLumberingHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void register(BreakEvent event) {
        // Check if enchantment is disabled
        if (!EnchantmentConfig.ENABLE_LUMBERING.get()) {
            return;
        }
        // Check that state, world, player or player's held item all exist
        if (event.getState() == null || event.getWorld() == null || event.getPlayer() == null
                || event.getPlayer().getMainHandItem().isEmpty() || event.getPlayer().isCreative()
                || !(event.getWorld() instanceof ServerLevel)) {
            return;
        }
        ServerLevel world = (ServerLevel) event.getWorld();
        Player player = event.getPlayer();
        // Checks if the axe has lumbering
        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.lumbering, player) > 0) {
            if (player.isCrouching()) {
                if (BlockTags.LOGS.contains(event.getState().getBlock())) {
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
                            if (BlockTags.LEAVES.contains(world.getBlockState(iterPos).getBlock())) {
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

    /**
     * Determines if the player can continue to chop down the tree
     *
     * @param player the player to check
     * @return
     */
    private boolean canStillChop(Player player) {
        if (!player.getMainHandItem().canPerformAction(ToolActions.AXE_DIG)) {
            return false;
        }
//        if (!player.getMainHandItem().getToolTypes().contains(ToolType.AXE)) {
//            return false;
//        }

        ItemStack axe = player.getMainHandItem().copy();
        IEnergyStorage cap = axe.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        // Energy Handling
        if (axe.hasTag()) {
            CompoundTag comp = axe.getTag();
            if (comp.contains("Energy")) {
                return comp.getInt("Energy") > 0;
            }
        }

        if (cap != null) {
            return cap.getEnergyStored() > 0;
        }

        // This will both save the tool, and save it for Imperishing
        return (axe.getMaxDamage() - axe.getDamageValue()) > 2;
    }

    private boolean woodMatchFilter(BlockState original, BlockState current) {
        if (EnchantmentConfig.LUMBERING_WOOD_STRICT.get()) {
            return original.getBlock().getRegistryName().equals(current.getBlock().getRegistryName());
        }
        return BlockTags.LOGS.contains(current.getBlock());
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