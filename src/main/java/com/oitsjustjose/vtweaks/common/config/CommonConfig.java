package com.oitsjustjose.vtweaks.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.tweaks.core.Tweak;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class CommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();

    static {
        var categories = Lists.newArrayList("block", "item");//, "item", "entity", "world")
        categories.forEach(catNm -> {
            COMMON_BUILDER.comment(catNm).push(catNm);
            VTweaks.getInstance().TweakRegistry.getAllTweaks().stream()
                    .filter(tweak -> tweak.getClass().getAnnotation(Tweak.class).category().equals(catNm))
                    .forEach(tweak -> tweak.registerConfigs(COMMON_BUILDER));
            COMMON_BUILDER.pop();
        });
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }
}