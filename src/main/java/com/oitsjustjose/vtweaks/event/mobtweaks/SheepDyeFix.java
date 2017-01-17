package com.oitsjustjose.vtweaks.event.mobtweaks;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class SheepDyeFix
{
	@SubscribeEvent
	public void registerEvent(EntityInteract event)
	{
		if (!VTweaks.config.enableSheepDyeFix)
			return;

		if (event.getTarget() == null || !(event.getTarget() instanceof EntitySheep))
			return;

		EntitySheep sheep = (EntitySheep) event.getTarget();
		EntityPlayer player = event.getEntityPlayer();

		if (!sheep.isChild() && !sheep.getSheared())
		{
			if (player.getHeldItemMainhand() != null)
			{
				int dyeColor = getDye(player.getHeldItemMainhand());

				if (dyeColor == -1)
					return;

				if (sheep.getFleeceColor() != EnumDyeColor.byDyeDamage(dyeColor))
				{
					player.getHeldItemMainhand().stackSize--;
					sheep.setFleeceColor(EnumDyeColor.byDyeDamage(dyeColor));
				}
			}
		}
	}

	int getDye(ItemStack itemstack)
	{
		// Checks if it's a blacklisted dye class first
		if (ignore(itemstack))
			return -1;

		// Otherwise continues to find the proper value
		int[] ids = OreDictionary.getOreIDs(itemstack);
		for (int i = 0; i < ids.length; i++)
		{
			String name = OreDictionary.getOreName(ids[i]);
			for (int meta = 0; meta < 16; meta++)
			{
				int[] dyeIDs = OreDictionary.getOreIDs(new ItemStack(Items.DYE, 1, meta));
				for (int j = 0; j < dyeIDs.length; j++)
					if (name.equalsIgnoreCase(OreDictionary.getOreName(dyeIDs[j])))
						return meta;
			}
		}
		return -1;
	}

	public boolean ignore(ItemStack itemstack)
	{
		Item stackItem = itemstack.getItem();
		for (String s : VTweaks.config.sheepDyeBlacklist)
			if (stackItem.getClass().getName().contains(s))
				return true;
		return false;
	}
}