package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.registries.VTweaksRegistry;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

@Tweak(category = "recipes")
public class NBTAnvilRecipe extends VTweak {

    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows use of datapacks to create NBT-strict anvil combination recipes. The built-in datapack does not add any recipes!\nSee https://mods.oitsjustjose.com/V-Tweaks/#anvil-recipes for datapack documentation.").define("enableDataDrivenAnvilRecipes", true);
    }

    @SubscribeEvent
    public void process(AnvilUpdateEvent evt) {
        if (!this.enabled.get()) return;

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
        return level.getRecipeManager().getRecipeFor(VTweaksRegistry.ANVIL_RECIPE_TYPE.get(), new RecipeWrapper(stackHandler), level).map(RecipeHolder::value);
    }
}
