package com.oitsjustjose.vtweaks.common.config;

import com.oitsjustjose.vtweaks.VTweaks;
import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.Builder;

public class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final String categoryName = "client";
    private static final Builder BUILDER = new Builder();

    static {
        var categories = VTweaks.getInstance().TweakRegistry.getClientTweaks().stream().map(VTweak::getCategory).sorted();

        categories.forEach(cat -> {
            BUILDER.push(cat);
            VTweaks.getInstance().TweakRegistry.getClientTweaks().stream().filter(tweak -> tweak.getCategory().equals(cat)).forEach(tweak -> tweak.registerConfigs(BUILDER));
            var popCount = cat.split("\\.").length;
            BUILDER.pop(popCount);
        });

        SPEC = BUILDER.build();
    }
}