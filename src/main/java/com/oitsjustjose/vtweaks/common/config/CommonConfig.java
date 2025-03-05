package com.oitsjustjose.vtweaks.common.config;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class CommonConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue EnableCactusMixin;
    public static final ModConfigSpec.BooleanValue EnableSplashPotionMixin;
    public static final ModConfigSpec.BooleanValue EnableItemMergingMixin;

    private static final Builder BUILDER = new Builder();

    static {
        var categories = VTweaks.getInstance().TweakRegistry.getCommonTweaks().stream().map(VTweak::getCategory).sorted();

        categories.forEach(cat -> {
            BUILDER.push(cat);
            VTweaks.getInstance().TweakRegistry.getCommonTweaks().stream().filter(tweak -> tweak.getCategory().equals(cat)).forEach(tweak -> tweak.registerConfigs(BUILDER));
            var popCount = cat.split("\\.").length;
            BUILDER.pop(popCount);
        });

        // Mixin Configs
        BUILDER.push("mixins");
        EnableCactusMixin = BUILDER.comment("If enabled, this tweak prevents cactus from destroying items that run into it").gameRestart().define("enableCactusItemProtection", true);
        EnableSplashPotionMixin = BUILDER.comment("If enabled, allows splash potions of water to behave the same as throwing an item in water via V-Tweaks' Fluid Conversion Recipes").gameRestart().define("enableSplashPotionTweak", true);
        EnableItemMergingMixin = BUILDER.comment("If enabled, items entities will no longer stack together like Minecraft Beta").gameRestart().define("enableNonStackingItemTweak", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}