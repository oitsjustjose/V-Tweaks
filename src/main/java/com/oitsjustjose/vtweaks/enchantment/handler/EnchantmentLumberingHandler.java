package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.ModConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
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
                        if (world.getBlockState(iterPos).getBlock().isWood(world, iterPos))
                        {
                            if (canStillChop(player))
                            {
                                world.destroyBlock(iterPos, true);
                                if (!chopTree(world, player, iterPos))
                                {
                                    return false;
                                }
                            }
                            else
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
        ItemStack axe = player.getHeldItemMainhand();
        // Check to make sure that the player is still holding the axe
        if (axe.isEmpty())
        {
            return false;
        }
        // Check to make sure that the player's held item still has lumbering
        else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().lumbering, axe) <= 0)
        {
            return false;
        }
        // Try to handle energy devices
        else if (axe.hasTagCompound())
        {
            NBTTagCompound comp = axe.getTagCompound();
            if (comp.hasKey("Energy"))
            {
                if (comp.getInteger("Energy") <= 0)
                {
                    return false;
                }
            }
        }
        // Another attempt at detecting energy-tools
        else if (axe.hasCapability(CapabilityEnergy.ENERGY, null))
        {
            if (axe.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() <= 0)
            {
                return false;
            }
        }
        // Handle imperishable logic here
        else if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().imperishable, axe) > 0)
        {
            VTweaks.LOGGER.info(axe.getMaxDamage() - axe.getItemDamage());
            if ((axe.getMaxDamage() - axe.getItemDamage()) <= 1)
            {
                return false;
            }
        }

        return !axe.attemptDamageItem(1, player.getRNG(), null);
    }
}