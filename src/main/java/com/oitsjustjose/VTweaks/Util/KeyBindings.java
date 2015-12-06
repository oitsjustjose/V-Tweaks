package com.oitsjustjose.VTweaks.Util;

import org.lwjgl.input.Keyboard;

import com.oitsjustjose.VTweaks.VTweaks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings
{
	public static KeyBinding extraInventory;
	public static KeyBinding crafting;

	public static void initialize()
	{
		crafting = new KeyBinding(StatCollector.translateToLocal("VTweaks.keybinding.crafting"), Keyboard.KEY_C, VTweaks.name);
		extraInventory = new KeyBinding(StatCollector.translateToLocal("VTweaks.keybinding.extraInventory"), Keyboard.KEY_V, VTweaks.name);
		ClientRegistry.registerKeyBinding(crafting);
		ClientRegistry.registerKeyBinding(extraInventory);
	}
}
