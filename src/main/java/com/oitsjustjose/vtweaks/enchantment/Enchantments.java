package com.oitsjustjose.vtweaks.enchantment;

import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentImperishableHandler;
import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentLumberingHandler;
import com.oitsjustjose.vtweaks.util.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class Enchantments
{
    private static Enchantments instance;
    public Enchantment lumbering;
    public Enchantment imperishable;
    private ArrayList<Enchantment> toRegister = new ArrayList<Enchantment>();

    private Enchantments()
    {
        init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Enchantments getInstance()
    {
        if (instance == null)
        {
            instance = new Enchantments();
        }
        return instance;
    }

    private void init()
    {

        if (ModConfig.enchantments.enableLumbering)
        {
            lumbering = new EnchantmentLumbering();
            registerEnchantment(lumbering);
            MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
        }

        if (ModConfig.enchantments.enableImperishable)
        {
            imperishable = new EnchantmentImperishable();
            registerEnchantment(imperishable);
            MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
        }

        if (ModConfig.enchantments.enableFeatherFallTweak)
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