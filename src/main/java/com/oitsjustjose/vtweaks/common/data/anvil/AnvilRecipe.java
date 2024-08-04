package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AnvilRecipe implements Recipe<RecipeWrapper> {
    private final ResourceLocation id;
    private final Ingredient left;
    private final Ingredient right;
    private final ItemStack result;
    private final int cost;
    private final boolean copyNbtFromLeft;
    private final boolean copyNbtFromRight;
    private final boolean strictMatch;

    public AnvilRecipe(ResourceLocation id, Ingredient leftIn, Ingredient rightIn, ItemStack resultIn, int xpCostIn, boolean copyComponentsFromLeftIn, boolean copyComponentsFromRightIn, boolean strictMatchIn) {
        this.id = id;
        this.left = leftIn;
        this.right = rightIn;
        this.result = resultIn;
        this.cost = xpCostIn;
        this.copyNbtFromLeft = copyComponentsFromLeftIn;
        this.copyNbtFromRight = copyComponentsFromRightIn;
        this.strictMatch = strictMatchIn;
        VTweaks.getInstance().addAnvilRecipe(id, this);
    }

    public ResourceLocation getId() {
        return this.id;
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

    public boolean copyComponentsFromLeft() {
        return this.copyNbtFromLeft;
    }

    public boolean copyComponentsFromRight() {
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

        boolean leftCompVerified = leftIngredientMatch.getComponents().isEmpty() && l.getComponents().isEmpty();
        if (!leftIngredientMatch.getComponents().isEmpty()) {
            leftCompVerified = !l.getComponents().isEmpty() && doComponentsMatch(leftIngredientMatch.getComponents(), l.getComponents());
        }

        boolean rightCompVerified = rightIngredientMatch.getComponents().isEmpty() && r.getComponents().isEmpty();
        if (!rightIngredientMatch.getComponents().isEmpty()) {
            rightCompVerified = !r.getComponents().isEmpty() && doComponentsMatch(rightIngredientMatch.getComponents(), r.getComponents());
        }

        return leftCompVerified && rightCompVerified;
    }

    @Override
    public ItemStack assemble(RecipeWrapper pInput, HolderLookup.Provider pRegistries) {
        return this.result;
    }

    private boolean doComponentsMatch(DataComponentMap orig, DataComponentMap comp) {
        // If these aren't the same size, there's no way they'll match.
        if (orig.size() == comp.size()) return false;

        for (var origKey : orig.keySet()) {
            var value = orig.get(origKey);

            if (comp.has(origKey)) {
                if (comp.get(origKey) != value) {
                    return false;
                }
            } else { /* The component to compare is missing a required key -- these don't match */
                return false;
            }
        }

        // We know that all required components exist and that they're the same sizes, so this is a 1:1 match!
        return true;
    }

    @Override
    public boolean canCraftInDimensions(int _a, int _b) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
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
