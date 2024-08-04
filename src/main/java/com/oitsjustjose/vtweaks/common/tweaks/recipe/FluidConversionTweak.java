package com.oitsjustjose.vtweaks.common.tweaks.recipe;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.entity.ConvertibleItemEntity;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.List;

@Tweak(category = "recipes")
public class FluidConversionTweak extends VTweak {

    public ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
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
        evt.setCanceled(true);
        evt.getEntity().discard();
    }

    public List<RecipeHolder<FluidConversionRecipe>> findRecipe(ItemTossEvent evt) {
        var level = evt.getPlayer().level();
        var stackHandler = new ItemStackHandler(1);
        stackHandler.setStackInSlot(0, evt.getEntity().getItem());
        return level.getRecipeManager().getRecipesFor(VTweaks.getInstance().CustomRecipeRegistry.FLUID_CONVERSION_RECIPE_TYPE, new RecipeWrapper(stackHandler), level);
    }
}
