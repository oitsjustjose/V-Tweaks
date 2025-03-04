package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipeInput;
import com.oitsjustjose.vtweaks.common.registries.ModRecipeTypes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

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
        var input = new AnvilRecipeInput(evt.getLeft(), evt.getRight());
        return level.getRecipeManager().getRecipeFor(ModRecipeTypes.ANVIL.get(), input, level).map(RecipeHolder::value);
    }
}
