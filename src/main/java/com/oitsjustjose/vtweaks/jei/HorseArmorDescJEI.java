// package com.oitsjustjose.vtweaks.jei;
//
// import mezz.jei.api.IJeiRuntime;
// import mezz.jei.api.IModPlugin;
// import mezz.jei.api.IModRegistry;
// import mezz.jei.api.JEIPlugin;
// import net.minecraft.init.Items;
// import net.minecraft.item.ItemStack;
//
// @JEIPlugin
// public class HorseArmorDescJEI implements IModPlugin
// {
// public static IJeiRuntime jeiHelper;
//
// @Override
// public void register(IModRegistry registry)
// {
// registry.addDescription(new ItemStack(Items.iron_horse_armor), "jei.desc.ironhorsearmor");
// registry.addDescription(new ItemStack(Items.golden_horse_armor), "jei.desc.goldhorsearmor");
// registry.addDescription(new ItemStack(Items.diamond_horse_armor), "jei.desc.diamondhorsearmor");
// }
//
// @Override
// public void onRuntimeAvailable(IJeiRuntime runtime)
// {
// jeiHelper = runtime;
// }
// }