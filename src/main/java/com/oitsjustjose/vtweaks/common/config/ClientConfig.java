package com.oitsjustjose.vtweaks.common.config;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class ClientConfig {
    public static final ModConfigSpec SPEC;
    private static final Builder BUILDER = new Builder();
    private static final String categoryName = "client";

    static {
        BUILDER.push(categoryName);
        VTweaks.getInstance().TweakRegistry.getAllTweaks().stream().filter(tweak -> tweak.getClass().getAnnotation(Tweak.class).category().equals(categoryName)).forEach(tweak -> tweak.registerConfigs(BUILDER));
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}