package com.oitsjustjose.VTweaks.Events;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.lwjgl.input.Keyboard;

import com.oitsjustjose.VTweaks.VTweaks;
import com.oitsjustjose.VTweaks.Util.Config;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ToolTips
{
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void registerTooltip(ItemTooltipEvent event)
	{
		if (event.itemStack == null)
			return;

		ItemStack stack = event.itemStack;
		ItemStack lavaBucket = new ItemStack(Items.lava_bucket);
		boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

		if (Config.autosmeltEnchantmentID > 0 && stack.getItem() == lavaBucket.getItem())
			if (shift)
			{
				event.toolTip.add(localize("tooltip.fire1"));
				event.toolTip.add(localize("tooltip.fire2"));
				event.toolTip.add(localize("tooltip.fire3"));
			}
			else
				event.toolTip.add(localize("tooltip.sneaking"));

		if (Config.unbreakableEnchantmentID > 0 && stack.getItem() == Items.nether_star)
			if (shift)
			{
				event.toolTip.add(localize("tooltip.netherstar1"));
				event.toolTip.add(localize("tooltip.netherstar2"));
				event.toolTip.add(localize("tooltip.netherstar3"));
			}
			else
				event.toolTip.add(localize("tooltip.sneaking"));
	}

	public static String localize(String string, boolean appendModID)
	{
		if (appendModID)
			string = VTweaks.modid + "." + string;
		return StatCollector.translateToLocal(string);
	}

	public static String localize(String string)
	{
		return localize(string, true);
	}
}