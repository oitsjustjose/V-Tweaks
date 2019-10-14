// package com.oitsjustjose.vtweaks.util;

// import java.util.ArrayList;
// import java.util.Objects;

// import com.google.common.collect.ImmutableList;
// import com.oitsjustjose.vtweaks.enchantment.Enchantments;

// import mezz.jei.api.IJeiHelpers;
// import mezz.jei.api.IJeiRuntime;
// import mezz.jei.api.IModPlugin;
// import mezz.jei.api.IModRegistry;
// import mezz.jei.api.ISubtypeRegistry;
// import mezz.jei.api.JEIPlugin;
// import mezz.jei.api.ingredients.IModIngredientRegistration;
// import mezz.jei.api.recipe.IVanillaRecipeFactory;
// import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
// import net.minecraft.init.Items;
// import net.minecraft.item.ItemStack;
// import net.minecraft.nbt.NBTTagCompound;
// import net.minecraft.util.ResourceLocation;
// import net.minecraftforge.fml.common.Loader;
// import net.minecraftforge.fml.common.registry.ForgeRegistries;

// @JEIPlugin
// public class JEICompat implements IModPlugin
// {
// @Override
// public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
// {
// }

// @Override
// public void registerIngredients(IModIngredientRegistration registry)
// {
// }

// @Override
// public void register(IModRegistry registry)
// {
// IJeiHelpers jeiHelpers = registry.getJeiHelpers();
// IVanillaRecipeFactory factory = jeiHelpers.getVanillaRecipeFactory();

// if (ModConfig.misc.enableHorseArmorRecipes)
// {
// addAnvilRecipe(factory, registry, new ItemStack(Items.IRON_LEGGINGS), new ItemStack(Items.IRON_LEGGINGS),
// new ItemStack(Items.IRON_HORSE_ARMOR));
// addAnvilRecipe(factory, registry, new ItemStack(Items.GOLDEN_LEGGINGS),
// new ItemStack(Items.GOLDEN_LEGGINGS), new ItemStack(Items.GOLDEN_HORSE_ARMOR));
// addAnvilRecipe(factory, registry, new ItemStack(Items.DIAMOND_LEGGINGS),
// new ItemStack(Items.DIAMOND_LEGGINGS), new ItemStack(Items.DIAMOND_HORSE_ARMOR));
// }
// if (ModConfig.enchantments.enableLumbering)
// {
// ArrayList<ItemStack> axes = new ArrayList<>();
// axes.add(new ItemStack(Items.GOLDEN_AXE));

// if (Loader.isModLoaded("toolbox"))
// {
// for (String s : new String[]
// { "null", "diamond", "emerald", "quartz", "prismarine", "ender_pearl", "lapis", "biotite", "amethyst",
// "ruby", "peridot", "topaz", "tanzanite", "malachite", "sapphire", "amber", "obsidian",
// "aquamarine" })
// {
// NBTTagCompound comp = new NBTTagCompound();
// comp.setString("ADORNMENT", s);
// comp.setString("Haft", "wood");
// comp.setString("Handle", "wood");
// comp.setString("Head", "gold");
// ItemStack toolboxAxe = new ItemStack(Objects
// .requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation("toolbox", "axe"))));
// toolboxAxe.setTagCompound(comp);
// axes.add(toolboxAxe);
// }
// }
// addAnvilRecipe(factory, registry, new ItemStack(Items.WRITABLE_BOOK), axes,
// HelperFunctions.getEnchantedBook(VTweaks.lumbering));
// }
// }

// @Override
// public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
// {
// }

// private void addAnvilRecipe(IVanillaRecipeFactory factory, IModRegistry registry, ItemStack inputLeft,
// ItemStack inputRight, ItemStack output)
// {
// ArrayList<ItemStack> rightTemp = new ArrayList<>();
// ArrayList<ItemStack> outputTemp = new ArrayList<>();
// rightTemp.add(inputRight);
// outputTemp.add(output);
// registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(inputLeft, rightTemp, outputTemp)),
// VanillaRecipeCategoryUid.ANVIL);
// }

// private void addAnvilRecipe(IVanillaRecipeFactory factory, IModRegistry registry, ItemStack inputLeft,
// ArrayList<ItemStack> inputRight, ItemStack output)
// {
// ArrayList<ItemStack> outputTemp = new ArrayList<>();
// for (int i = 0; i < inputRight.size(); i++)
// {
// outputTemp.add(output);
// }
// registry.addRecipes(ImmutableList.of(factory.createAnvilRecipe(inputLeft, inputRight, outputTemp)),
// VanillaRecipeCategoryUid.ANVIL);
// }
// }