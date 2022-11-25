package com.oitsjustjose.vtweaks.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;

@Mod.EventBusSubscriber
public class ClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    private static final Builder CLIENT_BUILDER = new Builder();

    static {
        var catNm = "client";
        CLIENT_BUILDER.push(catNm);
        VTweaks.getInstance().TweakRegistry.getAllTweaks().stream()
                .filter(tweak -> tweak.getClass().getAnnotation(Tweak.class).category().equals(catNm))
                .forEach(tweak -> tweak.registerConfigs(CLIENT_BUILDER));
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }
}