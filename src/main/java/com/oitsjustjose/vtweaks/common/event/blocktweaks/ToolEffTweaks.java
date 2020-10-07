package com.oitsjustjose.vtweaks.common.event.blocktweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.config.BlockTweakConfig;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ToolEffTweaks {
    @SubscribeEvent
    public void registerTweak(BreakSpeed event) {
        // Checks if feature is enabled
        if (!BlockTweakConfig.ENABLE_TOOL_EFF_TWEAKS.get()) {
            return;
        }
        // Checks that neither the block nor the held item are null

        if (event.getState() == null || event.getEntityLiving().getHeldItemMainhand().isEmpty()) {
            return;
        }

        Block block = event.getState().getBlock();
        ItemStack heldItem = event.getEntityLiving().getHeldItemMainhand();

        if (EnchantmentHelper.getEnchantmentLevel(VTweaks.imperishable, heldItem) > 0) {
            if (heldItem.getDamage() >= heldItem.getMaxDamage() - 1) {
                return;
            }
        }

        // Checks that the held item is a tool
        if (heldItem.getItem() instanceof ToolItem) {
            ToolItem tool = (ToolItem) heldItem.getItem();

            // Checks for axe-ing capabilities
            if (tool.getToolTypes(heldItem).contains(ToolType.AXE)) {
                if (block == Blocks.LADDER) {
                    event.setNewSpeed(event.getOriginalSpeed() * 5);
                }
            }
            // Checks for pickaxe-ing capabilities
            if (tool.getToolTypes(heldItem).contains(ToolType.PICKAXE)) {
                if (block.getDefaultState().getMaterial() == Material.GLASS) {
                    event.setNewSpeed(event.getOriginalSpeed() * 5);
                }
                if (block == Blocks.PACKED_ICE) {
                    event.setNewSpeed(event.getOriginalSpeed() * 5);
                }
                if (block instanceof SkullBlock) {
                    event.setNewSpeed(event.getOriginalSpeed() * 4);
                }
                if (block instanceof LeverBlock) {
                    event.setNewSpeed(event.getOriginalSpeed() * 4);
                }
                if (block.getDefaultState().getMaterial() == Material.PISTON) {
                    event.setNewSpeed(event.getOriginalSpeed() * 4);
                }
            }
        }
    }
}
