package com.oitsjustjose.vtweaks.util;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGUI extends GuiConfig
{
	public ConfigGUI(GuiScreen guiScreen)
	{
		super(guiScreen, getConfigElements(), VTweaks.modid, false, false, GuiConfig.getAbridgedConfigPath(Config.config.toString()));
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = Lists.newArrayList();

		list.add(new ConfigElement(Config.MobTweaks));
		list.add(new ConfigElement(Config.Enchantments));
		list.add(new ConfigElement(Config.BlockTweaks));
		list.add(new ConfigElement(Config.MiscFeatures));

		return list;
	}

	public static class GUIFactory implements IModGuiFactory
	{
		@Override
		public void initialize(Minecraft minecraftInstance)
		{

		}

		@Override
		public Class<? extends GuiScreen> mainConfigGuiClass()
		{
			return ConfigGUI.class;
		}

		@Override
		public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
		{
			return null;
		}

		@Override
		public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
		{
			return null;
		}
	}
}