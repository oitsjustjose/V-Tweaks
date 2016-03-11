package com.oitsjustjose.vtweaks.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oitsjustjose.vtweaks.VTweaks;

public class LogHelper
{
	private static Logger LOGGER = LogManager.getLogger(VTweaks.MODID);

	public LogHelper()
	{

	}

	public static void warn(String msg)
	{
		LOGGER.warn(msg);
	}

	public static void error(String msg)
	{
		LOGGER.error(msg);
	}

	public static void info(String msg)
	{
		LOGGER.info(msg);
	}

	public static void debug(String msg)
	{
		LOGGER.debug(msg);
	}
}