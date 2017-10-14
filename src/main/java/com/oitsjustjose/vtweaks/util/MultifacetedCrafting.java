package com.oitsjustjose.vtweaks.util;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class MultifacetedCrafting
{

    @SubscribeEvent
    public void register(ExplosionEvent event)
    {

        if (event.getExplosion() == null || event.getWorld() == null)
            return;

        World world = event.getWorld();
        List<BlockPos> locations = event.getExplosion().getAffectedBlockPositions();
        List<EntityItem> items = getItems(world, locations);
        BlockPos centerPos = new BlockPos(event.getExplosion().getPosition());

        if (!allItemsUndamaged(items))
        {
            System.out.println(("Not all items are undamaged"));
            return;
        }

        if (!allItemsPresent(items))
        {
            System.out.println(("Not all items are present"));
            return;
        }

        world.spawnEntity(HelperFunctions.createItemEntity(world, centerPos, HelperFunctions.getEnchantedBook(Enchantments.getInstance().multifaceted)));

        for (EntityItem i : items)
        {
            i.setDead();
        }

    }

    List<EntityItem> getItems(World world, List<BlockPos> positions)
    {
        List<EntityItem> retList = Lists.newArrayList();
        for (BlockPos pos : positions)
        {
            retList.addAll(world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1))));
        }
        return retList;
    }

    public boolean allItemsUndamaged(List<EntityItem> items)
    {
        for (EntityItem i : items)
        {
            if (i.getItem().getItemDamage() > 0)
            {
                return false;
            }
        }

        return true;
    }

    public boolean allItemsPresent(List<EntityItem> items)
    {
        boolean foundAxe = false;
        boolean foundShovel = false;
        boolean foundBook = false;

        for (EntityItem i : items)
        {

            if (i.getItem().getItem() == Items.DIAMOND_AXE)
            {
                foundAxe = true;
            }
            else if (i.getItem().getItem() == Items.DIAMOND_SHOVEL)
            {
                foundShovel = true;
            }
            else if (i.getItem().getItem() == Items.WRITABLE_BOOK)
            {
                foundBook = true;
            }
            else
            {
                return false;
            }
        }
        return foundAxe && foundShovel && foundBook;
    }
}
