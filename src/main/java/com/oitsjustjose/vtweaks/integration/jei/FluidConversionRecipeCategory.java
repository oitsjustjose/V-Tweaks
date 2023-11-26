package com.oitsjustjose.vtweaks.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.vtweaks.common.data.fluidconversion.FluidConversionRecipe;
import com.oitsjustjose.vtweaks.common.util.Constants;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class FluidConversionRecipeCategory implements IRecipeCategory<FluidConversionRecipe> {
    public static final RecipeType<FluidConversionRecipe> TYPE = RecipeType.create(Constants.MOD_ID, "fluid_conversion", FluidConversionRecipe.class);
    public static final ItemStack SPLASH_POTION = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.WATER);
    private final IDrawable background;
    private final IDrawable icon;

    public FluidConversionRecipeCategory(IGuiHelper guiHelper) {

        this.background = guiHelper.drawableBuilder(new ResourceLocation(Constants.MOD_ID, "textures/gui/fluid_conversion.png"), 0, 0, 76, 18).addPadding(0, 20, 32, 32).setTextureSize(76, 18).build();
        this.icon = guiHelper.createDrawableItemStack(SPLASH_POTION);
    }

    @Override
    @Nonnull
    public RecipeType<FluidConversionRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    @Nonnull
    public Component getTitle() {
        try {
            return new TranslatableContents("vtweaks.fluid_conversion.jei.title", null, new Object[]{}).resolve(null, null, 0);
        } catch (CommandSyntaxException ex) {
            return Component.empty();
        }
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, FluidConversionRecipe recipe, @NotNull IFocusGroup focuses) {
        var input = recipe.getInput();
        var fluid = ForgeRegistries.FLUIDS.getValue(recipe.getFluid());
        var output = recipe.getResult();
        if (fluid == null) return;

        var inputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 1 + 32, 1).addIngredients(input).setSlotName("inputSlot");
        var fluidSlot = builder.addSlot(RecipeIngredientRole.CATALYST, 1 + 32, 1).addFluidStack(fluid, 1000).setSlotName("fluidSlot");
        var outputSlot = builder.addSlot(RecipeIngredientRole.OUTPUT, 59 + 32, 1).addItemStack(output).setSlotName("outputSlot");
        // There is *no* auto-transfer for this, so there's nothing to really build a focus link for
    }

    @Override
    public void draw(FluidConversionRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        var fluid = ForgeRegistries.FLUIDS.getValue(recipe.getFluid());
        MutableComponent comp = Component.empty();
        try {
            var fluidNm = fluid.getFluidType().getDescription().getContents().resolve(null, null, 0);
            comp.append(new TranslatableContents("vtweaks.fluid_conversion.jei.text", fluidNm).resolve(null, null, 0));
        } catch (CommandSyntaxException ignored) {
        }

        var minecraft = Minecraft.getInstance();
        var width = minecraft.font.width(comp);
        var x = background.getWidth() - 2 - width;
        var y = 27;

        minecraft.font.draw(stack, comp, x + 0.5F, y + 0.5F, 0x25252525);
        minecraft.font.draw(stack, comp, x, y, 0x252525);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
