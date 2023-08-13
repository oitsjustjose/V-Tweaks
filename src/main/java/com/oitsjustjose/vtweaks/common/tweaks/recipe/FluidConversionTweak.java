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
        if (evt.getPlayer().level().isClientSide()) return;

        var recipes = findRecipe(evt);
        if (recipes.isEmpty()) return;

        var replacementItem = new ConvertibleItemEntity(evt.getEntity(), recipes);
        evt.getPlayer().level().addFreshEntity(replacementItem);
        evt.setResult(Event.Result.DENY);
        evt.setCanceled(true);
        evt.getEntity().discard();
    }

    public List<FluidConversionRecipe> findRecipe(ItemTossEvent evt) {
        var level = evt.getPlayer().level();
        var recipes = level.getRecipeManager().getAllRecipesFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE);
        // Recipes use a stack size of 1, so set it to 1 to actually get a good compare
        var searchStack = evt.getEntity().getItem().copy();
        searchStack.setCount(1);
        return recipes.stream().filter(x -> x.getInput().equals(searchStack, true)).collect(Collectors.toList());
    }
}
