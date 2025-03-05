package com.oitsjustjose.vtweaks.integration.jei;

import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.util.Constants;
import com.oitsjustjose.vtweaks.common.util.I18n;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FluidConversionRecipeCategory implements IRecipeCategory<FluidConversionRecipe> {
    public static final RecipeType<FluidConversionRecipe> TYPE = RecipeType.create(Constants.MOD_ID, "fluid_conversion", FluidConversionRecipe.class);
    public static final ItemStack SPLASH_POTION = PotionContents.createItemStack(Items.SPLASH_POTION, Potions.WATER.getDelegate());
    private final IDrawable background;
    private final IDrawable icon;

    public FluidConversionRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/fluid_conversion.png"), 0, 0, 76, 18).addPadding(0, 20, 32, 32).setTextureSize(76, 18).build();
        this.icon = guiHelper.createDrawableItemStack(SPLASH_POTION);
    }

    @Override
    public int getWidth() {
        return this.background.getWidth();
    }

    @Override
    public int getHeight() {
        return this.background.getHeight();
    }

    @Override
    @Nonnull
    public RecipeType<FluidConversionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        return I18n.Translate("vtweaks.fluid_conversion.jei.title");
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, FluidConversionRecipe recipe, @NotNull IFocusGroup focuses) {
        var input = recipe.getInput();
        var fluid = BuiltInRegistries.FLUID.get(recipe.getFluid());
        var output = recipe.getResult();

        builder.addSlot(RecipeIngredientRole.INPUT, 1 + 32, 1).addIngredients(input).setSlotName("inputSlot");
        builder.addSlot(RecipeIngredientRole.CATALYST, 1 + 32, 1).addFluidStack(fluid, 1000).setSlotName("fluidSlot");
        builder.addSlot(RecipeIngredientRole.OUTPUT, 59 + 32, 1).addItemStack(output).setSlotName("outputSlot");
        // There is *no* auto-transfer for this, so there's nothing to really build a focus link for
    }

    @Override
    public void draw(FluidConversionRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);

        var fluid = BuiltInRegistries.FLUID.get(recipe.getFluid());
        var fluidNm = I18n.Resolve(fluid.getFluidType().getDescription().getContents());
        var comp = I18n.Translate("vtweaks.fluid_conversion.jei.text", fluidNm);

        var minecraft = Minecraft.getInstance();
        var width = minecraft.font.width(comp);
        var x = background.getWidth() - 2 - width;
        var y = 27;

        guiGraphics.drawString(minecraft.font, comp, x + 1, y + 1, 0x25252525);
        guiGraphics.drawString(minecraft.font, comp, x, y, 0x252525);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}
