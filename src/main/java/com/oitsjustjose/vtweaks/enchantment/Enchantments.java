package com.oitsjustjose.vtweaks.enchantment;

import java.util.ArrayList;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Enchantments
{
	public Enchantment hypermending;
	public Enchantment autosmelt;
	public Enchantment stepboost;
	public Enchantment lumbering;
	public Enchantment imperishable;
	private ArrayList<Enchantment> toRegister = new ArrayList<Enchantment>();

	public Enchantments()
	{
		init();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void init()
	{
		if (VTweaks.config.enableEnchHypermending)
		{
			hypermending = new EnchantmentHypermending();
			registerEnchantment(hypermending);
		}

		if (VTweaks.config.enableEnchAutosmelt)
		{
			autosmelt = new EnchantmentAutosmelt();
			registerEnchantment(autosmelt);
		}

		if (VTweaks.config.enableEnchStepboost)
		{
			stepboost = new EnchantmentStepboost();
			registerEnchantment(stepboost);
		}

		if (VTweaks.config.enableEnchLumbering)
		{
			lumbering = new EnchantmentLumbering();
			registerEnchantment(lumbering);
		}

		if (VTweaks.config.enableEnchImperishable)
		{
			imperishable = new EnchantmentImperishable();
			registerEnchantment(imperishable);
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
			event.getRegistry().register(e);
	}
}