package com.oitsjustjose.vtweaks.common.data.anvil;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilRecipeSerializer implements RecipeSerializer<AnvilRecipe> {
    private ResourceLocation rl = new ResourceLocation(Constants.MODID, "anvil");

    @Override
    public @NotNull AnvilRecipe fromJson(@NotNull ResourceLocation rl, @NotNull JsonObject obj) {
        ItemStack left = deserialize(obj, "left");
        ItemStack right = deserialize(obj, "right");
        ItemStack result = deserialize(obj, "result");
        int cost = obj.get("cost").getAsInt();
        return new AnvilRecipe(rl, left, right, result, cost);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull AnvilRecipe recipe) {
        buf.writeItemStack(recipe.getLeft(), false);
        buf.writeItemStack(recipe.getRight(), false);
        buf.writeItemStack(recipe.getResult(), false);
        buf.writeInt(recipe.getCost());
    }

    @Override
    public @Nullable AnvilRecipe fromNetwork(@NotNull ResourceLocation rl, @NotNull FriendlyByteBuf buf) {
        ItemStack left = buf.readItem();
        ItemStack right = buf.readItem();
        ItemStack result = buf.readItem();
        int cost = buf.readInt();
        return new AnvilRecipe(rl, left, right, result, cost);
    }

    private ItemStack deserialize(JsonObject parent, String key) {
        try {
            return ShapedRecipe.itemStackFromJson(parent.getAsJsonObject(key));
        } catch (JsonSyntaxException ex) {
            VTweaks.getInstance().LOGGER.error("Item {} does not exist", parent.get(key).toString());
            ex.printStackTrace();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
        this.rl = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.rl;
    }

    @Override
    public Class<RecipeSerializer<?>> getRegistryType() {
        return AnvilRecipeSerializer.castClass();
    }

    @SuppressWarnings("unchecked")
    private static <G> Class<G> castClass() {
        return (Class<G>) RecipeSerializer.class;
    }

}
