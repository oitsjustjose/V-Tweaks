package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.entity.ConvertibleItemEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

    @SubscribeEvent
    public void process(ItemTossEvent evt) {
        if (!this.enabled.get()) return;
        if (evt.getPlayer().level().isClientSide()) return;

        var recipe = findRecipe(evt);
        if (recipe.isEmpty()) return;
        var replacementItem = new ConvertibleItemEntity(evt.getEntity(), recipe.get().getResult(), recipe.get().getFluid());
        evt.getPlayer().level().addFreshEntity(replacementItem);

        evt.setResult(Event.Result.DENY);
        evt.setCanceled(true);
        evt.getEntity().discard();
    }

    public Optional<FluidConversionRecipe> findRecipe(ItemTossEvent evt) {
        var level = evt.getPlayer().level();
        var handler = new ItemStackHandler(1);
        handler.setStackInSlot(0, evt.getEntity().getItem());
        return level.getRecipeManager().getRecipeFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(handler), level);
    }
}
