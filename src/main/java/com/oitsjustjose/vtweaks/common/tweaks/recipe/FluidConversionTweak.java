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

import java.util.List;
import java.util.stream.Collectors;

@Tweak(category = "recipes")
public class FluidConversionTweak extends VTweak {

    public ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("Allows you to throw things into specific fluids to convert them - by default this is just Concrete Powder this feature can be extended using Data Packs.").define("enableFluidConversionRecipes", true);
    }

    @SubscribeEvent
    public void process(ItemTossEvent evt) {
        if (!this.enabled.get()) return;
        if (evt.getPlayer().getLevel().isClientSide()) return;

        var recipes = findRecipe(evt);
        if (recipes.isEmpty()) return;

        var replacementItem = new ConvertibleItemEntity(evt.getEntity(), recipes);
        evt.getPlayer().getLevel().addFreshEntity(replacementItem);
        evt.setResult(Event.Result.DENY);
        evt.setCanceled(true);
        evt.getEntity().discard();
    }

    public List<FluidConversionRecipe> findRecipe(ItemTossEvent evt) {
        var level = evt.getPlayer().getLevel();
        var stackHandler = new ItemStackHandler(1);
        stackHandler.setStackInSlot(0, evt.getEntity().getItem());
        return level.getRecipeManager().getRecipesFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(stackHandler), level);
    }
}
