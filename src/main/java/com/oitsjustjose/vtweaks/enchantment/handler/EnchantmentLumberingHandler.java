package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.ModConfig;

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
                || event.getPlayer().getHeldItemMainhand().isEmpty() || event.getPlayer().capabilities.isCreativeMode)
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
            if (player.isSneaking())
            {
                if (event.getState().getBlock().isWood(world, event.getPos()))
                {
                    chopTree(world, player, event.getPos());
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
    private boolean chopTree(World world, EntityPlayer player, BlockPos pos)
    {
        for (int mod_x = -1; mod_x <= 1; mod_x++)
        {
            for (int mod_y = -1; mod_y <= 1; mod_y++)
            {
                for (int mod_z = -1; mod_z <= 1; mod_z++)
                {
                    BlockPos iterPos = pos.add(mod_x, mod_y, mod_z);
                    if (iterPos != pos)
                    {
                        if (!canStillChop(player))
                        {
                            return false;
                        }

                        if (world.getBlockState(iterPos).getBlock().isWood(world, iterPos))
                        {
                            world.destroyBlock(iterPos, true);
                            player.getHeldItemMainhand().attemptDamageItem(1, player.getRNG(), null);
                            if (!chopTree(world, player, iterPos))
                            {
                                return false;
                            }
                        }
                        else if (ModConfig.enchantments.lumberingCutsLeaves)
                        {
                            if (world.getBlockState(iterPos).getBlock().isLeaves(world.getBlockState(iterPos), world,
                                    iterPos))
                            {
                                world.destroyBlock(iterPos, true);
                                if (!chopTree(world, player, iterPos))
                                {
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
    private boolean canStillChop(EntityPlayer player)
    {
        if (player.getHeldItemMainhand().isEmpty())
        {
            return false;
        }
        else if (player.getHeldItemMainhand().getItemDamage() >= player.getHeldItemMainhand().getMaxDamage() - 2)
        {
            return false;
        }
        else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().lumbering,
                player.getHeldItemMainhand()) <= 0)
        {
            return false;
        }
        return true;
    }
}