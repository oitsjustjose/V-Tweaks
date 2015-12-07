package com.oitsjustjose.vtweaks.util;

import org.lwjgl.input.Keyboard;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings
{
	public static KeyBinding extraInventory;
	public static KeyBinding crafting;

	public static void initialize()
	{
		crafting = new KeyBinding(StatCollector.translateToLocal("vtweaks.keybinding.crafting"), Keyboard.KEY_C, VTweaks.name);
		extraInventory = new KeyBinding(StatCollector.translateToLocal("vtweaks.keybinding.extraInventory"), Keyboard.KEY_V, VTweaks.name);
		ClientRegistry.registerKeyBinding(crafting);
		ClientRegistry.registerKeyBinding(extraInventory);
	}
}
