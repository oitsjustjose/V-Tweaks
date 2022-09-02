package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public class AnvilRecipeHandler {
    @SubscribeEvent
    public void registerEvent(AnvilUpdateEvent evt) {
        Optional<AnvilRecipe> r = find(evt);
        if (r.isPresent()) {
            evt.setOutput(r.get().getResult());
            evt.setCost(r.get().getCost());
        }
    }

    public Optional<AnvilRecipe> find(AnvilUpdateEvent evt) {
        Level l = evt.getPlayer().getLevel();
        ItemStackHandler e = new ItemStackHandler(2);
        e.setStackInSlot(0, evt.getLeft());
        e.setStackInSlot(1, evt.getRight());
        return l.getRecipeManager().getRecipeFor(VTweaks.RecipeSerializers.ANVIL_RECIPE_TYPE, new RecipeWrapper(e), l);
    }
}
