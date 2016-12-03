package com.oitsjustjose.vtweaks.event.mobtweaks;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class SheepDyeFix
{
	@SubscribeEvent
	public void registerEvent(EntityInteract event)
	{
		if (event.getTarget() == null || !(event.getTarget() instanceof EntitySheep))
			return;

		EntitySheep sheep = (EntitySheep) event.getTarget();
		EntityPlayer player = event.getEntityPlayer();

		if (!sheep.isChild() && !sheep.getSheared())
		{
			if (player.getHeldItemMainhand() != null && getDye(player.getHeldItemMainhand()) != -1)
			{
				if (!(player.getHeldItemMainhand().getItem().getClass().getName().contains("biomesoplenty")))
				{
					player.getHeldItemMainhand().setCount(player.getHeldItemMainhand().getCount() - 1);
					sheep.setFleeceColor(EnumDyeColor.byDyeDamage(getDye(player.getHeldItemMainhand())));
				}
			}
		}
	}

	int getDye(ItemStack itemstack)
	{
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
}