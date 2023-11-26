package com.oitsjustjose.vtweaks.common.data.anvil;

import com.google.gson.JsonObject;
import com.oitsjustjose.vtweaks.common.data.helpers.VTJsonHelpers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilRecipeSerializer implements RecipeSerializer<AnvilRecipe> {
    @Override
    public @NotNull AnvilRecipe fromJson(@NotNull ResourceLocation rl, @NotNull JsonObject obj) {
        var left = Ingredient.fromJson(obj.get("left"), false);
        var right = Ingredient.fromJson(obj.get("right"), false);
        var result = VTJsonHelpers.deserializeItemStack(obj, "result");
        var cost = obj.get("cost").getAsInt();
        var cpl = obj.has("copyTagsFromLeft") && obj.get("copyTagsFromLeft").getAsBoolean();
        var cpr = obj.has("copyTagsFromRight") && obj.get("copyTagsFromRight").getAsBoolean();
        var strict = obj.has("strict") && obj.get("strict").getAsBoolean();
        return new AnvilRecipe(rl, left, right, result, cost, cpl, cpr, strict);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull AnvilRecipe recipe) {
        CraftingHelper.write(buf, recipe.getLeft());
        CraftingHelper.write(buf, recipe.getRight());
        buf.writeItemStack(recipe.getResult(), false);
        buf.writeInt(recipe.getCost());
        buf.writeBoolean(recipe.shouldResultCopyNbtFromLeft());
        buf.writeBoolean(recipe.shouldResultCopyNbtFromRight());
        buf.writeBoolean(recipe.isStrictMatch());
    }

    @Override
    public @Nullable AnvilRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
        var left = Ingredient.fromNetwork(buf);
        var right = Ingredient.fromNetwork(buf);
        var result = buf.readItem();
        var cost = buf.readInt();
        var cpl = buf.readBoolean();
        var cpr = buf.readBoolean();
        var strict = buf.readBoolean();
        return new AnvilRecipe(id, left, right, result, cost, cpl, cpr, strict);
    }
}
