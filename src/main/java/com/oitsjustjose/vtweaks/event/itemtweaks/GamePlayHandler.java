package com.oitsjustjose.vtweaks.event.itemtweaks;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.enchantment.Enchantments;
import com.oitsjustjose.vtweaks.util.Recipes;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class GamePlayHandler
{
	public static void initialize()
	{
		if(!VTweaks.config.enableGamePlayHandler)
			return;
		
		MinecraftForge.EVENT_BUS.register(new GamePlayHandler());

		Recipes.removeRecipe(new ItemStack(Items.STONE_SWORD));
		Recipes.removeRecipe(new ItemStack(Items.STONE_PICKAXE));
		Recipes.removeRecipe(new ItemStack(Items.STONE_SHOVEL));
		Recipes.removeRecipe(new ItemStack(Items.STONE_AXE));
		Recipes.removeRecipe(new ItemStack(Items.STONE_HOE));

		Items.WOODEN_SWORD.setMaxDamage(16);
		Items.WOODEN_PICKAXE.setMaxDamage(16);
		Items.WOODEN_SHOVEL.setMaxDamage(16);
		Items.WOODEN_AXE.setMaxDamage(16);
		Items.WOODEN_HOE.setMaxDamage(16);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_SWORD), "#", "#", "S", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_PICKAXE), "###", " S ", " S ", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_SHOVEL), "#", "S", "S", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_AXE), "## ", "#S ", " S ", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_AXE), "## ", "S# ", "S  ", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_HOE), "##", "S ", "S ", '#', Items.FLINT, 'S', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.COBBLESTONE, 2), "##", "##", '#', Blocks.GRAVEL));
	}

	@SubscribeEvent
	public void registerTweak(HarvestDropsEvent event)
	{
		if (event.getHarvester() == null)
			return;
		if (event.isSilkTouching() || hasAutoSmelt(event.getHarvester().getHeldItemMainhand()))
			return;

		if (event.getState().getBlock() == Blocks.STONE)
		{
			if (event.getState().getBlock().getMetaFromState(event.getState()) == 0)
			{
				event.getDrops().clear();
				if (event.getWorld().rand.nextInt(10 - event.getFortuneLevel() * 3) == 0)
					event.getDrops().add(new ItemStack(Items.FLINT));
				else
					event.getDrops().add(new ItemStack(Blocks.GRAVEL));
			}
		}
	}
	
	boolean hasAutoSmelt(ItemStack itemstack)
	{
		if (VTweaks.config.autosmeltID == 0 || itemstack == null)
			return false;

		int autosmeltLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.autosmelt, itemstack);

		if (autosmeltLevel > 0)
			return true;

		return false;
	}
}