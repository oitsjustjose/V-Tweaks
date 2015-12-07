package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.util.Config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModGUIConfig extends GuiConfig
{
	public ModGUIConfig(GuiScreen guiScreen)
	{
		super(guiScreen, new ConfigElement(Config.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), VTweaks.modid, false, true,
				GuiConfig.getAbridgedConfigPath(Config.config.toString()));
	}
}