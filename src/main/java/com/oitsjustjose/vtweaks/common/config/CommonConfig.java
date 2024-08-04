package com.oitsjustjose.vtweaks.common.config;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class CommonConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue EnableCactusMixin;
    public static final ModConfigSpec.BooleanValue EnableSplashPotionMixin;

    private static final Builder BUILDER = new Builder();

    static {
        var categories = VTweaks.getInstance().TweakRegistry.getAllTweaks().stream().map(tweak -> tweak.getClass().getAnnotation(Tweak.class).category()).sorted();

        categories.forEach(catNm -> {
            BUILDER.push(catNm);
            VTweaks.getInstance().TweakRegistry.getAllTweaks().stream().filter(tweak -> tweak.getClass().getAnnotation(Tweak.class).category().equals(catNm)).forEach(tweak -> tweak.registerConfigs(BUILDER));
            BUILDER.pop();
        });


        // Mixin Configs
        BUILDER.comment("All changes made here require a game restart.").push("mixins");
        EnableCactusMixin = BUILDER.comment("If enabled, this tweak prevents cactus from destroying items that run into it").define("enableCactusItemProtection", true);
        EnableSplashPotionMixin = BUILDER.comment("If enabled, allows splash potions of water to behave the same as throwing an item in water via V-Tweaks' Fluid Conversion Recipes").define("enableSplashPotionTweak", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}