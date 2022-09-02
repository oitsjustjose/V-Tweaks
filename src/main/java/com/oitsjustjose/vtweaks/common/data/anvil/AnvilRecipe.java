package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.util.JEICompat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

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

        /* Have to manually check the NBT because of weirdness */
        if (l.getItem() != this.left.getItem()) return false;
        if (r.getItem() != this.right.getItem()) return false;

        boolean leftTagVerified = !this.left.hasTag();
        if (this.left.hasTag() && this.left.getTag() != null) {
            leftTagVerified = l.hasTag() && bareMinimumCompare(this.left.getTag(), l.getTag());
        }

        boolean rightTagVerified = !this.right.hasTag();
        if (this.right.hasTag() && this.right.getTag() != null) {
            rightTagVerified = r.hasTag() && bareMinimumCompare(this.right.getTag(), r.getTag());
        }

        return leftTagVerified && rightTagVerified;
    }

    /* Does a check to see if an ItemStack matches on AT LEAST the tags+values from the recipe */
    private boolean bareMinimumCompare(CompoundTag orig, CompoundTag comp) {
        return orig.getAllKeys().stream().allMatch(key -> compareTags(orig, comp, key));
    }

    private boolean compareTags(CompoundTag orig, CompoundTag comp, String key) {
        if (!comp.contains(key)) return false;

        switch (orig.getTagType(key)) {
            case Tag.TAG_BYTE:
                return orig.getByte(key) == comp.getByte(key);
            case Tag.TAG_SHORT:
                return orig.getShort(key) == comp.getShort(key);
            case Tag.TAG_INT:
                return orig.getInt(key) == comp.getInt(key);
            case Tag.TAG_LONG:
                return orig.getLong(key) == comp.getLong(key);
            case Tag.TAG_FLOAT:
                return orig.getFloat(key) == comp.getFloat(key);
            case Tag.TAG_DOUBLE:
                return orig.getDouble(key) == comp.getDouble(key);
            case Tag.TAG_BYTE_ARRAY:
                return Arrays.equals(orig.getByteArray(key), comp.getByteArray(key));
            case Tag.TAG_STRING:
                return orig.getString(key).equals(comp.getString(key));
            case Tag.TAG_LIST:
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
            case Tag.TAG_COMPOUND:
                return bareMinimumCompare(orig.getCompound(key), comp.getCompound(key));
            case Tag.TAG_INT_ARRAY:
                return Arrays.equals(orig.getIntArray(key), comp.getIntArray(key));
            case Tag.TAG_LONG_ARRAY:
                return Arrays.equals(orig.getLongArray(key), comp.getLongArray(key));
        }

        VTweaks.getInstance().LOGGER.info("Received tag ID of {} which is not recognized", orig.getId());
        return false;
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
