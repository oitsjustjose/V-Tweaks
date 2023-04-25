package com.oitsjustjose.vtweaks.common.tweaks.item;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Locale;

@Tweak(eventClass = ItemTooltipEvent.class, category = "item")
public class FoodTooltipTweak extends VTweak {
    private ForgeConfigSpec.EnumValue<TooltipSetting> setting;
    private ForgeConfigSpec.DoubleValue multiplier;
    private ForgeConfigSpec.ConfigValue<String> simpleColor;
    private ForgeConfigSpec.ConfigValue<String> buffColor;
    private ForgeConfigSpec.ConfigValue<String> debuffColor;
    private ForgeConfigSpec.ConfigValue<String> saturationColor;
    private ForgeConfigSpec.BooleanValue useOriginalFoodTooltipColor;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.setting = builder.comment("Show food hunger & saturation on item hover").defineEnum("foodTooltipSetting", TooltipSetting.WITH_SHIFT);
        this.multiplier = builder.comment("Modifies the number of hunger & saturation points rendered (specifically for Hunger Strike).\n" + "The original hunger value is multiplied by this value, so 42x means 2 times as much hunger/saturation will render, and 0.5 means half as much hunger/saturation will render.").defineInRange("foodTooltipMultiplier", 1.0, 0.0, Float.MAX_VALUE);
        this.simpleColor = builder.comment("Modifies the color of a tooltip for a food that gives no effects on consumption.\nUses HEX Web Colors which you can pick from here: https://dv2ls.com/colpic").define("foodTooltipColor", "#00AA00");
        this.buffColor = builder.comment("Modifies the color of a tooltip for a food that gives buffs on consumption.\nUses HEX Web Colors which you can pick from here: https://dv2ls.com/colpic").define("foodTooltipPositiveColor", "#AA00AA");
        this.debuffColor = builder.comment("Modifies the color of a tooltip for a food that gives debuffs on consumption.\nUses HEX Web Colors which you can pick from here: https://dv2ls.com/colpic").define("foodTooltipNegativeColor", "#AA0000");
        this.saturationColor = builder.comment("Modifies the color of the saturation tooltip.\nUses HEX Web Colors which you can pick from here: https://dv2ls.com/colpic").define("foodTooltipSaturationColor", "#AA0000");
        this.useOriginalFoodTooltipColor = builder.comment("If a food has a custom color, use that color instead of the defined color regardless of the buffs/debuffs the food gives").define("useOriginalFoodTooltipColor", true);
    }

    @SubscribeEvent
    public void process(ItemTooltipEvent evt) {
        if (this.setting.get() == TooltipSetting.NEVER) return;

        var stack = evt.getItemStack();
        var food = stack.getFoodProperties(evt.getEntity());
        var shifting = Screen.hasShiftDown();
        if (!stack.isEdible() || food == null) return;
        if (this.setting.get() == TooltipSetting.WITH_SHIFT && !shifting) return;

        evt.getToolTip().add(getHungerString(stack, food, stack.getRarity()));
        evt.getToolTip().add(getSaturationString((int) (food.getSaturationModifier() * 10 * multiplier.get()), stack.getRarity()));
    }

    private boolean hasBadEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> !x.getFirst().getEffect().isBeneficial());
    }

    private boolean hasGoodEffect(List<Pair<MobEffectInstance, Float>> e) {
        return e.stream().anyMatch(x -> x.getFirst().getEffect().isBeneficial());
    }

    private MutableComponent getHungerString(ItemStack stack, FoodProperties food, Rarity rarity) {
        var nutrition = (int) (food.getNutrition() * multiplier.get());

        // Determine color based on stack and food effects
        var color = simpleColor.get();
        if (useOriginalFoodTooltipColor.get() && stack.getHoverName().getStyle().getColor() != null) {
            color = String.format(Locale.ROOT, "#%06X", stack.getHoverName().getStyle().getColor().getValue());
        } else if (useOriginalFoodTooltipColor.get() && stack.getDisplayName().getStyle().getColor() != null &&
                stack.getDisplayName().getStyle().getColor().getValue() != 16777215) {
            color = String.format(Locale.ROOT, "#%06X", stack.getDisplayName().getStyle().getColor().getValue());
        } else if (hasBadEffect(food.getEffects())) {
            color = debuffColor.get();
        } else if (hasGoodEffect(food.getEffects())) {
            color = buffColor.get();
        }

        var style = Style.EMPTY.withColor(TextColor.parseColor(color)).withFont(Style.DEFAULT_FONT);
        rarity.getStyleModifier().apply(style);

        var ret = new StringBuilder();
        ret.append("\u2588".repeat(Math.max(0, (nutrition / 2))));
        if (nutrition % 2 != 0) ret.append("\u258C");

        var t = new TranslatableContents("vtweaks.hunger.tooltip.text", "--", new Object[]{ret.toString()});
        try {
            return t.resolve(null, null, 0).setStyle(style);
        } catch (CommandSyntaxException e) {
            return Component.empty();
        }
    }

    private MutableComponent getSaturationString(int saturation, Rarity rarity) {
        var style = Style.EMPTY.withColor(TextColor.parseColor(saturationColor.get())).withFont(Style.DEFAULT_FONT);
        rarity.getStyleModifier().apply(style);

        var ret = new StringBuilder();
        ret.append("\u2588".repeat(Math.max(0, saturation / 2)));
        if (saturation % 2 != 0) ret.append("\u258C");

        var t = new TranslatableContents("vtweaks.saturation.tooltip.text", "--", new Object[]{ret.toString()});
        try {
            return t.resolve(null, null, 0).setStyle(style);
        } catch (CommandSyntaxException e) {
            return Component.empty();
        }
    }

    public enum TooltipSetting {
        NEVER, WITH_SHIFT, ALWAYS
    }
}
