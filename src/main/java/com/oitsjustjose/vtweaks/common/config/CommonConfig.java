package com.oitsjustjose.vtweaks.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class CommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.BooleanValue EnableCactusMixin;
    public static final ForgeConfigSpec.BooleanValue EnableSplashPotionMixin;
    private static final Builder COMMON_BUILDER = new Builder();

    static {
        var categories = VTweaks.getInstance().TweakRegistry.getAllTweaks().stream().map(tweak -> tweak.getClass().getAnnotation(Tweak.class).category()).sorted();

        categories.forEach(catNm -> {
            COMMON_BUILDER.push(catNm);
            VTweaks.getInstance().TweakRegistry.getAllTweaks().stream().filter(tweak -> tweak.getClass().getAnnotation(Tweak.class).category().equals(catNm)).forEach(tweak -> tweak.registerConfigs(COMMON_BUILDER));
            COMMON_BUILDER.pop();
        });


        // Mixin Configs
        COMMON_BUILDER.comment("All changes made here require a game restart.").push("mixins");
        EnableCactusMixin = COMMON_BUILDER.comment("If enabled, this tweak prevents cactus from destroying items that run into it").define("enableCactusItemProtection", true);
        EnableSplashPotionMixin = COMMON_BUILDER.comment("If enabled, allows splash potions of water to behave the same as throwing an item in water via V-Tweaks' Fluid Conversion Recipes").define("enableSplashPotionTweak", true);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }
}