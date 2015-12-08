package com.oitsjustjose.vtweaks.util;

import org.lwjgl.input.Keyboard;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings
{
	public static KeyBinding extraInventory;

	public static void initialize()
	{
		extraInventory = new KeyBinding(StatCollector.translateToLocal("vtweaks.keybinding.extraInventory"), Keyboard.KEY_V, VTweaks.name);
		ClientRegistry.registerKeyBinding(extraInventory);
	}
}
