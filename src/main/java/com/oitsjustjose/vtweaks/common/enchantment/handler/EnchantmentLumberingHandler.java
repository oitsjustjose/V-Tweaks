package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
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
                || event.getPlayer().getHeldItemMainhand().isEmpty() || event.getPlayer().isCreative()
                || !(event.getWorld() instanceof ServerWorld)) {
            return;
        }
        ServerWorld world = (ServerWorld) event.getWorld();
        PlayerEntity player = event.getPlayer();
        // Checks if the axe has lumbering
        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.lumbering, player.getHeldItemMainhand()) > 0) {
            if (player.isCrouching()) {
                if (BlockTags.LOGS.contains(event.getState().getBlock())) {
                    chopTree(world, player, event.getPos(), event.getState(), 0);
                }
            }
        }
    }

    /**
     * A recursive chop-tree function (I made this myself! :D)
     * 
     * @param world
     * @param state
     * @param player
     * @param pos
     */
    private boolean chopTree(ServerWorld world, PlayerEntity player, BlockPos pos, BlockState original, int curr) {
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
                    BlockPos iterPos = pos.add(mod_x, mod_y, mod_z);
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
    private boolean canStillChop(PlayerEntity player) {
        if (!player.getHeldItemMainhand().getToolTypes().contains(ToolType.AXE)) {
            return false;
        }

        ItemStack axe = player.getHeldItemMainhand().copy();
        IEnergyStorage cap = axe.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        // Energy Handling
        if (axe.hasTag()) {
            CompoundNBT comp = axe.getTag();
            if (comp.contains("Energy")) {
                return comp.getInt("Energy") > 0;
            }
        }

        if (cap != null) {
            return cap.getEnergyStored() > 0;
        }

        // This will both save the tool, and save it for Imperishing
        return (axe.getMaxDamage() - axe.getDamage()) > 2;
    }

    private boolean woodMatchFilter(BlockState original, BlockState current) {
        if (EnchantmentConfig.LUMBERING_WOOD_STRICT.get()) {
            return original.getBlock().getRegistryName().equals(current.getBlock().getRegistryName());
        }
        return BlockTags.LOGS.contains(current.getBlock());
    }

    private void emulateBreak(ServerWorld world, BlockPos pos, PlayerEntity player, boolean damageTool) {
        BlockState s = world.getBlockState(pos);

        s.getBlock().onBlockHarvested(world, pos, s, player);
        Block.spawnDrops(s, world, pos, world.getTileEntity(pos));
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        if (damageTool) {
            player.getHeldItemMainhand().attemptDamageItem(1, player.getRNG(), null);
        }
    }
}