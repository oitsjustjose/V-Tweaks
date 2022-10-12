package com.oitsjustjose.vtweaks.common.data.anvil;

import com.oitsjustjose.vtweaks.VTweaks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
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
            ItemStack output = r.get().getResult();

            if (r.get().shouldResultCopyNbtFromLeft()) {
                CompoundTag oTag = output.getTag();
                CompoundTag lTag = evt.getLeft().getTag();
                if (lTag != null && oTag != null) {
                    output.setTag(lTag.merge(oTag));
                }
            }

            if (r.get().shouldResultCopyNbtFromRight()) {
                CompoundTag oTag = output.getTag();
                CompoundTag rTag = evt.getRight().getTag();
                if (rTag != null && oTag != null) {
                    output.setTag(rTag.merge(oTag));
                }
            }

            evt.setOutput(output);
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
