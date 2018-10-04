package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentLumberingHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void register(BreakEvent event)
    {
        // Check if enchantment is disabled
        if (!ModConfig.enchantments.enableLumbering)
        {
            return;
        }
        // Check that state, world, player or player's held item all exist
        if (event.getState() == null || event.getWorld() == null || event.getPlayer() == null
                || event.getPlayer().getHeldItemMainhand().isEmpty())
        {
            return;
        }
        // Local variables
        World world = event.getWorld();
        EntityPlayer player = event.getPlayer();
        // Checks if the axe has lumbering
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().lumbering,
                player.getHeldItemMainhand()) > 0)
        {
            chopTree(world, player, event.getPos());
        }
    }

    // Simple function that chops an 11w x 11l x 32h range of logs down
    // Does not do any actual tree detection. Not I'm not that good.
    private void chopTree(World world, EntityPlayer player, BlockPos pos)
    {
        // Checks if player is sneaking, and block (and block above) are log blocks
        if (player.isSneaking() && world.getBlockState(pos).getBlock().isWood(world, pos))
        {
            for (int xPos = pos.getX() - 5; xPos <= pos.getX() + 5; xPos++)
            {
                for (int yPos = pos.getY() - 1; yPos <= pos.getY() + 32; yPos++)
                {
                    for (int zPos = pos.getZ() - 5; zPos <= pos.getZ() + 5; zPos++)
                    {
                        BlockPos newPos = new BlockPos(xPos, yPos, zPos);
                        if (world.getBlockState(newPos).getBlock().isWood(world, newPos))
                        {
                            if (player.getHeldItemMainhand().attemptDamageItem(1, world.rand, null))
                            {
                                return;
                            }
                            breakBlock(world, player, newPos);
                        }
                    }
                }
            }
        }
    }

    // Simple function to break a block as if a player did it
    private void breakBlock(World world, EntityPlayer player, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        IBlockState state = world.getBlockState(pos);

        block.onBlockHarvested(world, pos, state, player);
        if (block.removedByPlayer(state, world, pos, player, true))
        {
            block.onBlockDestroyedByPlayer(world, pos, state);
            block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());
        }
        world.setBlockToAir(pos);
        world.playSound(null, pos, block.getSoundType(state, world, pos, player).getBreakSound(), SoundCategory.BLOCKS,
                0.25F, 0.8F);
    }
}