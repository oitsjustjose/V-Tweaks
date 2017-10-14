package com.oitsjustjose.vtweaks.event.itemtweaks;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WoodItemFuelHandler
{
    @SubscribeEvent
    public void registerfuels(FurnaceFuelBurnTimeEvent event)
    {
        Item item = event.getItemStack().getItem();

        if (item instanceof ItemDoor && item != Items.IRON_DOOR)
        {
            event.setBurnTime(600);
        }
        else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
        {
            event.setBurnTime(300);
        }
        else if (item == Items.SIGN)
        {
            event.setBurnTime(600);
        }
        else if (item == Item.getItemFromBlock(Blocks.LADDER))
        {
            event.setBurnTime(200);
        }
        else if (item instanceof ItemBoat)
        {
            event.setBurnTime(1500);
        }
    }
}