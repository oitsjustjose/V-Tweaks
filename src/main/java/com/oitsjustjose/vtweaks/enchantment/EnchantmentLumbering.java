package com.oitsjustjose.vtweaks.enchantment;

import java.util.Set;

import com.oitsjustjose.vtweaks.VTweaks;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantmentLumbering extends Enchantment
{
	protected EnchantmentLumbering()
	{
		super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
		Enchantment.REGISTRY.register(VTweaks.config.lumberingID, new ResourceLocation(VTweaks.MODID, "lumbering"), this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean canApplyTogether(Enchantment enchantment)
	{
		return true;
	}

	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() instanceof ItemAxe;
	}

	@Override
	public int getMinEnchantability(int par1)
	{
		return 0;
	}

	@Override
	public int getMaxEnchantability(int par1)
	{
		return 0;
	}

	@SubscribeEvent
	public void fixApplication(AnvilUpdateEvent event)
	{
		if (event.getLeft() == null || event.getRight() == null)
			return;

		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (!Enchantments.bookHasEnchantment(right, this) || !(left.getItem() instanceof ItemTool))
			return;

		Set<String> classes = ((ItemTool) left.getItem()).getToolClasses(left);
		if (!classes.contains("axe"))
		{
			event.setCost(0);
			event.setOutput(left.copy());
		}
	}
}
