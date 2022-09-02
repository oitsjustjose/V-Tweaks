package com.oitsjustjose.vtweaks.common.registry;

import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipeSerializer;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipeType;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeRegistrator {
    public final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);
    public final RecipeSerializer<AnvilRecipe> ANVIL;
    public final RecipeType<AnvilRecipe> ANVIL_RECIPE_TYPE;

    public RecipeRegistrator() {
        this.ANVIL = new AnvilRecipeSerializer();
        SERIALIZERS.register("anvil", () -> this.ANVIL);
        this.ANVIL_RECIPE_TYPE = new AnvilRecipeType();
    }

}
