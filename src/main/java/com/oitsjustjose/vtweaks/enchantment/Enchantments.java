package com.oitsjustjose.vtweaks.enchantment;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.config.EnchantmentConfig;
import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentImperishableHandler;
import com.oitsjustjose.vtweaks.enchantment.handler.EnchantmentLumberingHandler;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Enchantments
{
    private static Enchantments instance;
    public Enchantment lumbering;
    public Enchantment imperishable;
    private ArrayList<Enchantment> toRegister = new ArrayList<Enchantment>();

    private Enchantments()
    {
        init();
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
        if (EnchantmentConfig.ENABLE_LUMBERING.get())
        {
            lumbering = new EnchantmentLumbering();
            registerEnchantment(lumbering);
            MinecraftForge.EVENT_BUS.register(new EnchantmentLumberingHandler());
        }

        if (EnchantmentConfig.ENABLE_IMPERISHABLE.get())
        {
            imperishable = new EnchantmentImperishable();
            registerEnchantment(imperishable);
            MinecraftForge.EVENT_BUS.register(new EnchantmentImperishableHandler());
        }

        if (EnchantmentConfig.ENABLE_FF_TWEAK.get())
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