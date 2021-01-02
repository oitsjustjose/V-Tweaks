package com.oitsjustjose.vtweaks.common.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.EnchantmentConfig;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
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
                || event.getPlayer().getHeldItemMainhand().isEmpty() || event.getPlayer().isCreative()) {
            return;
        }
        // Local variables
        IWorld world = event.getWorld();
        PlayerEntity player = event.getPlayer();
        // Checks if the axe has lumbering
        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.lumbering, player.getHeldItemMainhand()) > 0) {
            if (player.isCrouching()) {
                if (BlockTags.LOGS.contains(event.getState().getBlock())) {
                    chopTree(world, player, event.getPos(), event.getState());
                    world.playSound(
                            null, event.getPos(), event.getState().getBlock()
                                    .getSoundType(event.getState(), world, event.getPos(), player).getBreakSound(),
                            SoundCategory.BLOCKS, 0.25F, 0.8F);
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
    private boolean chopTree(IWorld world, PlayerEntity player, BlockPos pos, BlockState original) {
        for (int mod_x = -1; mod_x <= 1; mod_x++) {
            for (int mod_y = -1; mod_y <= 1; mod_y++) {
                for (int mod_z = -1; mod_z <= 1; mod_z++) {
                    BlockPos iterPos = pos.add(mod_x, mod_y, mod_z);
                    if (iterPos != pos) {
                        if (!canStillChop(player)) {
                            return false;
                        }
                        if (woodMatchFilter(original, world.getBlockState(iterPos))) {
                            world.destroyBlock(iterPos, true);
                            player.getHeldItemMainhand().attemptDamageItem(1, player.getRNG(), null);

                            if (!chopTree(world, player, iterPos, original)) {
                                return false;
                            }
                        } else if (EnchantmentConfig.LUMBERING_CUTS_LEAVES.get()) {
                            if (BlockTags.LEAVES.contains(world.getBlockState(iterPos).getBlock())) {
                                world.destroyBlock(iterPos, true);

                                if (!chopTree(world, player, iterPos, original)) {
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
        ItemStack axe = player.getHeldItemMainhand().copy();
        IEnergyStorage cap = axe.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        if (axe.hasTag()) {
            CompoundNBT comp = axe.getTag();
            if (comp.contains("Energy")) {
                if (comp.getInt("Energy") <= 0) {
                    return false;
                }
            }
        }

        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, axe) != 0) {
            if ((axe.getMaxDamage() - axe.getDamage()) <= 2) {
                return false;
            }
        }

        if (cap != null && cap.getEnergyStored() <= 0) {
            return false;
        }

        return (axe.getMaxDamage() - axe.getDamage()) > 2;
    }

    private boolean woodMatchFilter(BlockState original, BlockState current) {
        if (EnchantmentConfig.LUMBERING_WOOD_STRICT.get()) {
            return original.equals(current);
        }
        return BlockTags.LOGS.contains(current.getBlock());
    }
}