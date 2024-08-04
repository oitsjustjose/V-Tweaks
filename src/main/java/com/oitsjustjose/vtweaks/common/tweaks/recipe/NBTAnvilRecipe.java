package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

@Tweak(category = "recipe")
public class NBTAnvilRecipe extends VTweak {
    @SubscribeEvent
    public void process(AnvilUpdateEvent evt) {
        var recipe = find(evt);
        if (recipe.isEmpty()) return;

        var output = recipe.get().getResult().copy();
        if (recipe.get().copyComponentsFromLeft()) {
            output.applyComponents(evt.getLeft().copy().getComponents());
        }

        if (recipe.get().copyComponentsFromRight()) {
            output.applyComponents(evt.getRight().copy().getComponents());
        }

        evt.setOutput(output);
        evt.setCost(recipe.get().getCost());
    }

    public Optional<AnvilRecipe> find(AnvilUpdateEvent evt) {
        var level = evt.getPlayer().level();
        var stackHandler = new ItemStackHandler(2);
        stackHandler.setStackInSlot(0, evt.getLeft());
        stackHandler.setStackInSlot(1, evt.getRight());
        return level.getRecipeManager().getRecipeFor(VTweaks.getInstance().CustomRecipeRegistry.ANVIL_RECIPE_TYPE, new RecipeWrapper(stackHandler), level).map(RecipeHolder::value);
    }
}
