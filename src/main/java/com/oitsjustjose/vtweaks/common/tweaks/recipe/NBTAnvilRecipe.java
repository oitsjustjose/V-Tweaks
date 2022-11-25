package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.data.anvil.AnvilRecipe;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

@Tweak(eventClass = AnvilUpdateEvent.class, category = "recipe")
public class NBTAnvilRecipe extends VTweak {
    @Override
    public void process(Event event) {
        var evt = (AnvilUpdateEvent) event;
        var r = find(evt);
        if (r.isEmpty()) return;

        var output = r.get().getResult().copy();

        if (r.get().shouldResultCopyNbtFromLeft()) {
            var oTag = output.getTag();
            var lTag = evt.getLeft().copy().getTag();
            if (lTag != null && oTag != null) {
                output.setTag(lTag.copy().merge(oTag));
            }
        }

        if (r.get().shouldResultCopyNbtFromRight()) {
            var oTag = output.getTag();
            var rTag = evt.getRight().copy().getTag();
            if (rTag != null && oTag != null) {
                output.setTag(rTag.copy().merge(oTag));
            }
        }

        evt.setOutput(output);
        evt.setCost(r.get().getCost());
    }

    public Optional<AnvilRecipe> find(AnvilUpdateEvent evt) {
        var level = evt.getPlayer().getLevel();
        var stackHandler = new ItemStackHandler(2);
        stackHandler.setStackInSlot(0, evt.getLeft());
        stackHandler.setStackInSlot(1, evt.getRight());
        return level.getRecipeManager().getRecipeFor(VTweaks.getInstance().CustomRecipeRegistry.ANVIL_RECIPE_TYPE, new RecipeWrapper(stackHandler), level);
    }
}
