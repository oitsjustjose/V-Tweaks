package com.oitsjustjose.vtweaks.common.core;

import net.neoforged.neoforge.common.ModConfigSpec;


public abstract class VTweak {
    public void registerConfigs(ModConfigSpec.Builder builder) {
    }

    public String getCategory() {
        var annotation = this.getClass().getAnnotation(Tweak.class);
        assert annotation != null;
        return annotation.category();
    }
}
