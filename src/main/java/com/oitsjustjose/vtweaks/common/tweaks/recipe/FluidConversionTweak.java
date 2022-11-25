package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.entity.ConvertibleItemEntity;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

@Tweak(eventClass = ItemTossEvent.class, category = "recipes")
public class FluidConversionTweak extends VTweak {

    public ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows you to throw things into specific fluids to convert them - by default this is just Concrete Powder this feature can be extended using Data Packs.").define("enableFluidConversionRecipes", true);
    }

    @Override
    public void process(Event event) {
        if (!this.enabled.get()) return;

        var evt = (ItemTossEvent) event;
        if (evt.getPlayer().getLevel().isClientSide()) return;

        var recipe = findRecipe(evt);
        if (recipe.isEmpty()) return;
        var replacementItem = new ConvertibleItemEntity(evt.getEntity(), recipe.get().getResult(), recipe.get().getFluid());
        evt.getPlayer().getLevel().addFreshEntity(replacementItem);

        evt.setResult(Event.Result.DENY);
        evt.setCanceled(true);
        evt.getEntity().discard();
    }

    public Optional<FluidConversionRecipe> findRecipe(ItemTossEvent evt) {
        var level = evt.getPlayer().getLevel();
        var handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, evt.getEntity().getItem());
        return level.getRecipeManager().getRecipeFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(handler), level);
    }
}
