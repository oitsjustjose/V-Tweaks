package com.oitsjustjose.vtweaks.util;

import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;

public class Recipes
{
    @SubscribeEvent
    public void registerHorseArmorRecipes(AnvilUpdateEvent event)
    {
        if (!ModConfig.misc.enableHorseArmorRecipes || event.getLeft().isEmpty() || event.getRight().isEmpty())
            return;

        Item left = event.getLeft().getItem();
        Item right = event.getRight().getItem();

        boolean damaged = event.getLeft().getItemDamage() > 0 || event.getRight().getItemDamage() > 0;

        if ((left == Items.DIAMOND_LEGGINGS && right == Items.DIAMOND_LEGGINGS) && !damaged)
        {
            event.setCost(12);
            event.setOutput(new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        }
        if ((left == Items.GOLDEN_LEGGINGS && right == Items.GOLDEN_LEGGINGS) && !damaged)
        {
            event.setCost(8);
            event.setOutput(new ItemStack(Items.GOLDEN_HORSE_ARMOR));
        }
        if ((left == Items.IRON_LEGGINGS && right == Items.IRON_LEGGINGS) && !damaged)
        {
            event.setCost(4);
            event.setOutput(new ItemStack(Items.IRON_HORSE_ARMOR));
        }
    }

    @SubscribeEvent
    public void registerBookRecipes(AnvilUpdateEvent event)
    {

        if (ModConfig.enchantments.enableLumbering)
        {
            ItemStack book = HelperFunctions.getEnchantedBook(Enchantments.getInstance().lumbering);

            if (event.getLeft().isEmpty() || event.getRight().isEmpty())
                return;

            boolean damaged = event.getRight().getItemDamage() > 0;

            if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == Items.GOLDEN_AXE)
            {
                if (!damaged)
                {
                    event.setCost(ModConfig.enchantments.lumberingCost);
                    event.setOutput(book);
                }
                else
                {
                    event.setOutput(ItemStack.EMPTY);
                }
            }
            else if (Loader.isModLoaded("toolbox"))
            {
                Item toolboxAxe = Objects
                        .requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("toolbox", "axe")));
                if (event.getLeft().getItem() == Items.WRITABLE_BOOK && event.getRight().getItem() == toolboxAxe)
                {
                    if (event.getRight().hasTagCompound() && event.getRight().getTagCompound() != null)
                    {
                        NBTTagCompound comp = event.getRight().getTagCompound();
                        if (comp.getString("Head").equals("gold") && event.getRight().getItemDamage() == 0)
                        {
                            event.setCost(ModConfig.enchantments.lumberingCost);
                            event.setOutput(book);
                        }
                        else
                        {
                            event.setOutput(ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
}
