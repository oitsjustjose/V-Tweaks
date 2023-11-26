package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AnvilRecipe implements Recipe<RecipeWrapper> {
    public final ResourceLocation id;
    private final Ingredient left;
    private final Ingredient right;
    private final ItemStack result;
    private final int cost;
    private final boolean copyNbtFromLeft;
    private final boolean copyNbtFromRight;
    private final boolean strictMatch;

    public AnvilRecipe(ResourceLocation id, Ingredient l, Ingredient r, ItemStack e, int c, boolean cpl, boolean cpr, boolean strict) {
        this.id = id;
        this.left = l;
        this.right = r;
        this.result = e;
        this.cost = c;
        this.copyNbtFromLeft = cpl;
        this.copyNbtFromRight = cpr;
        this.strictMatch = strict;
        VTweaks.getInstance().addAnvilRecipe(id, this);
    }

    public Ingredient getLeft() {
        return this.left;
    }

    public Ingredient getRight() {
        return this.right;
    }

    public ItemStack getResult() {
        return this.result.copy();
    }

    public int getCost() {
        return this.cost;
    }

    public boolean shouldResultCopyNbtFromLeft() {
        return this.copyNbtFromLeft;
    }

    public boolean shouldResultCopyNbtFromRight() {
        return this.copyNbtFromRight;
    }

    public boolean isStrictMatch() {
        return this.strictMatch;
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
        var l = wrapper.getItem(0);
        var r = wrapper.getItem(1);
        if (l == null || r == null) return false;

        if (!(this.left.test(l) && this.right.test(r))) return false;

        /* Recipe inputs match so that's enough for us :) */
        if (!isStrictMatch()) return true;

        var leftIngredientMatch = Arrays.stream(this.left.getItems()).filter(x -> x.getItem() == l.getItem()).findFirst().orElse(ItemStack.EMPTY);
        var rightIngredientMatch = Arrays.stream(this.right.getItems()).filter(x -> x.getItem() == r.getItem()).findFirst().orElse(ItemStack.EMPTY);

        if (rightIngredientMatch.isEmpty() || leftIngredientMatch.isEmpty()) {
            VTweaks.getInstance().LOGGER.error("Anvil recipe [{} + {}] met an invalid state in AnvilRecipes#matches", l.toString(), r.toString());
            return false;
        }

        // If the left item is expected to have a tag
        boolean leftTagVerified = !leftIngredientMatch.hasTag() && !l.hasTag();
        if (leftIngredientMatch.hasTag() && leftIngredientMatch.getTag() != null) {
            leftTagVerified = l.hasTag() && bareMinimumCompare(leftIngredientMatch.getTag(), l.getTag());
        }

        boolean rightTagVerified = !rightIngredientMatch.hasTag() && !r.hasTag();
        if (rightIngredientMatch.hasTag() && rightIngredientMatch.getTag() != null) {
            rightTagVerified = r.hasTag() && bareMinimumCompare(rightIngredientMatch.getTag(), r.getTag());
        }

        return leftTagVerified && rightTagVerified;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper wrapper) {
        return this.result;
    }

    /* Does a check to see if an ItemStack matches on AT LEAST the tags+values from the recipe */
    private boolean bareMinimumCompare(CompoundTag orig, CompoundTag comp) {
        return orig.getAllKeys().stream().allMatch(key -> compareTags(orig, comp, key));
    }

    private boolean compareTags(CompoundTag orig, CompoundTag comp, String key) {
        if (!comp.contains(key)) return false;

        switch (orig.getTagType(key)) {
            case Tag.TAG_BYTE -> {
                return orig.getByte(key) == comp.getByte(key);
            }
            case Tag.TAG_SHORT -> {
                return orig.getShort(key) == comp.getShort(key);
            }
            case Tag.TAG_INT -> {
                return orig.getInt(key) == comp.getInt(key);
            }
            case Tag.TAG_LONG -> {
                return orig.getLong(key) == comp.getLong(key);
            }
            case Tag.TAG_FLOAT -> {
                return orig.getFloat(key) == comp.getFloat(key);
            }
            case Tag.TAG_DOUBLE -> {
                return orig.getDouble(key) == comp.getDouble(key);
            }
            case Tag.TAG_BYTE_ARRAY -> {
                return Arrays.equals(orig.getByteArray(key), comp.getByteArray(key));
            }
            case Tag.TAG_STRING -> {
                return orig.getString(key).equals(comp.getString(key));
            }
            case Tag.TAG_LIST -> {
                ListTag origList = (ListTag) orig.get(key);
                ListTag compList = (ListTag) comp.get(key);
                if (origList == null || compList == null) return false;
                if (origList.getElementType() != Tag.TAG_COMPOUND) {
                    VTweaks.getInstance().LOGGER.info("List tag of type {} is not supported", origList.getElementType());
                    return false;
                }

                // Iterate over every REQUIRED tag in orig
                for (int i = 0; i < origList.size(); i++) {
                    boolean anyMatchedForThisEntry = false;
                    CompoundTag t1 = origList.getCompound(i);
                    // Iterate over every tag in comp - some may be extra / not req'd, that's fine
                    for (int j = 0; j < compList.size(); j++) {
                        CompoundTag t2 = compList.getCompound(j);
                        anyMatchedForThisEntry = bareMinimumCompare(t1, t2);
                    }
                    if (!anyMatchedForThisEntry) return false;
                }
                return true;
            }
            case Tag.TAG_COMPOUND -> {
                return bareMinimumCompare(orig.getCompound(key), comp.getCompound(key));
            }
            case Tag.TAG_INT_ARRAY -> {
                return Arrays.equals(orig.getIntArray(key), comp.getIntArray(key));
            }
            case Tag.TAG_LONG_ARRAY -> {
                return Arrays.equals(orig.getLongArray(key), comp.getLongArray(key));
            }
        }

        VTweaks.getInstance().LOGGER.info("Received tag ID of {} which is not recognized", orig.getId());
        return false;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return VTweaks.getInstance().CustomRecipeRegistry.ANVIL;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return VTweaks.getInstance().CustomRecipeRegistry.ANVIL_RECIPE_TYPE;
    }
}
