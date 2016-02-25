package com.oitsjustjose.vtweaks.jei;

import mezz.jei.api.IItemRegistry;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class HorseArmorDescJEI implements IModPlugin
{
    public static IJeiHelpers jeiHelper;

    @Override
    public void register(IModRegistry registry)
    {
        registry.addDescription(new ItemStack(Items.iron_horse_armor), "jei.desc.ironhorsearmor");
        registry.addDescription(new ItemStack(Items.golden_horse_armor), "jei.desc.goldhorsearmor");
        registry.addDescription(new ItemStack(Items.diamond_horse_armor), "jei.desc.diamondhorsearmor");
    }

    @Override
    public void onJeiHelpersAvailable(IJeiHelpers jeiHelpers)
    {
        jeiHelper = jeiHelpers;
    }

    @Override
    public void onItemRegistryAvailable(IItemRegistry itemRegistry)
    {

    }

    @Override
    public void onRecipeRegistryAvailable(IRecipeRegistry recipeRegistry)
    {

    }

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		// TODO Auto-generated method stub
		
	}
}