package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.JEICompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class AnvilRecipe implements Recipe<RecipeWrapper> {
    public final ResourceLocation id;
    private final ItemStack left;
    private final ItemStack right;
    private final ItemStack result;
    private final int cost;

    public AnvilRecipe(ResourceLocation id, ItemStack l, ItemStack r, ItemStack e, int c) {
        this.id = id;
        this.left = l;
        this.right = r;
        this.result = e;
        this.cost = c;
        JEICompat.cache.put(id, this);
    }

    public ItemStack getLeft() {
        return this.left.copy();
    }

    public ItemStack getRight() {
        return this.right.copy();
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        ItemStack l = wrapper.getItem(0);
        ItemStack r = wrapper.getItem(1);
        if (l == null || r == null) return false;

        // TODO: This isn't working - figure that out 
        /* Have to manually check the NBT because of weirdness */
        if (l.getItem() != this.left.getItem()) return false;
        if (r.getItem() != this.right.getItem()) return false;
        /* If left and recipe's left have tags, compare them */
        if (l.getTag() != null && this.left.getTag() != null) {
            String s = l.getTag().;
            String s1 = this.left.getTag().getAsString();
            if (!l.getTag().getAsString().equals(this.left.getTag().getAsString())) return false;
        }
        /* If right and recipe's right have tags, compare them */
        if (r.getTag() != null && this.right.getTag() != null) {
            if (!r.getTag().getAsString().equals(this.right.getTag().getAsString())) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper wrapper) {
        return this.result;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTweaks.RecipeSerializers.ANVIL;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return VTweaks.RecipeSerializers.ANVIL_RECIPE_TYPE;
    }
}
