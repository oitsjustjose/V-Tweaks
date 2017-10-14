package com.oitsjustjose.vtweaks.enchantment.handler;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.HelperFunctions;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

public class EnchantmentMultifacetedHandler
{
    private final HashSet<Material> acceptedMaterials;

    public EnchantmentMultifacetedHandler()
    {
        acceptedMaterials = new HashSet<>();
        acceptedMaterials.add(Material.LEAVES);
        acceptedMaterials.add(Material.GLASS);
        acceptedMaterials.add(Material.PISTON);
        acceptedMaterials.add(Material.GRASS);
        acceptedMaterials.add(Material.GROUND);
        acceptedMaterials.add(Material.CACTUS);
        acceptedMaterials.add(Material.SAND);
        acceptedMaterials.add(Material.WOOD);
        acceptedMaterials.add(Material.VINE);
        acceptedMaterials.add(Material.SPONGE);
        acceptedMaterials.add(Material.SNOW);
        acceptedMaterials.add(Material.REDSTONE_LIGHT);
        acceptedMaterials.add(Material.ICE);
        acceptedMaterials.add(Material.CRAFTED_SNOW);
        acceptedMaterials.add(Material.CLOTH);
        acceptedMaterials.add(Material.PACKED_ICE);
        acceptedMaterials.add(Material.GOURD);
        acceptedMaterials.add(Material.CIRCUITS);
        acceptedMaterials.add(Material.CARPET);
    }


    @SubscribeEvent
    public void register(BreakSpeed event)
    {
        if (event.getEntityPlayer() == null || event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).isEmpty())
            return;

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.getInstance().multifaceted, event.getEntityPlayer().getHeldItemMainhand()) > 0)
        {
            if(shouldTrigger(event.getState()))
            {
                event.setNewSpeed(event.getOriginalSpeed() * 5 + (5 * EnchantmentHelper.getEnchantmentLevel(HelperFunctions.getEnchantment("minecraft", "efficiency"), event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND))));
            }
        }
    }

    private boolean shouldTrigger(IBlockState state)
    {
        return state.getMaterial().isToolNotRequired() || acceptedMaterials.contains(state.getMaterial());
    }
}

