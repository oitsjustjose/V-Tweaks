package com.oitsjustjose.vtweaks.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class MixinConfig {
    public static final ForgeConfigSpec MIXIN_CONFIG;
    public static final ForgeConfigSpec.BooleanValue EnableCactusMixin;
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    static {
        /* Ugh, I didn't want this */
        COMMON_BUILDER.comment("All changes made here require a game restart.").push("mixins");
        EnableCactusMixin = COMMON_BUILDER.comment("If enabled, this tweak prevents cactus from destroying items that run into it").define("enableCactusItemProtection", true);
        COMMON_BUILDER.pop();

        MIXIN_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }
}
