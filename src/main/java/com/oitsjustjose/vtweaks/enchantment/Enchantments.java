package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.enchantment.handler.*;
import com.oitsjustjose.vtweaks.util.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;


/**
 *





 */
public class Enchantments
{
    private static Enchantments instance;
    public Enchantment hypermending;
    public Enchantment autosmelt;
    public Enchantment stepboost;
    public Enchantment lumbering;
    public Enchantment imperishable;
    public Enchantment multifaceted;
    private ArrayList<Enchantment> toRegister = new ArrayList<Enchantment>();

    public Enchantments()
    {
        init();
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public static Enchantments getInstance()
    {
        return instance;
    }

    private void init()
    {
        if (Config.getInstance().enableEnchHypermending)
        {
            hypermending = new EnchantmentHypermending();
            registerEnchantment(hypermending);
            MinecraftForge.EVENT_BUS.register(new EnchantmentHypermendingHandler());
        }

        if (Config.getInstance().enableEnchAutosmelt)
        {
            autosmelt = new EnchantmentAutosmelt();
            registerEnchantment(autosmelt);
            MinecraftForge.EVENT_BUS.register(new EnchantmentAutosmeltHandler());
        }

        if (Config.getInstance().enableEnchStepboost)
        {
            stepboost = new EnchantmentStepboost();
            registerEnchantment(stepboost);
            MinecraftForge.EVENT_BUS.register(new EnchantmentStepboostHandler());
        }

        if (Config.getInstance().enableEnchLumbering)
        {
            lumbering = new EnchantmentLumbering();
            registerEnchantment(lumbering);
            MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
        }

        if (Config.getInstance().enableEnchImperishable)
        {
            imperishable = new EnchantmentImperishable();
            registerEnchantment(imperishable);
            MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
        }

        if (Config.getInstance().enableEnchMultifaceted)
        {
            multifaceted = new EnchantmentMultifaceted();
            registerEnchantment(multifaceted);
            MinecraftForge.EVENT_BUS.register(new EnchantmentMultifacetedHandler());
        }

        if (Config.getInstance().enableFeatherFallingTweak)
        {
            MinecraftForge.EVENT_BUS.register(new FeatherFallingTweak());
        }
    }

    public void registerEnchantment(Enchantment ench)
    {
        toRegister.add(ench);
    }

    @SubscribeEvent
    public void handleRegistry(RegistryEvent.Register<Enchantment> event)
    {
        for (Enchantment e : toRegister)
        {
            event.getRegistry().register(e);
        }
    }
}