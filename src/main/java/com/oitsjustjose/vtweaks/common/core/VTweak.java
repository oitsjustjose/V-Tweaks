package com.oitsjustjose.vtweaks.common.core;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.Event;


public abstract class VTweak {
    public abstract void process(Event event);

    public void registerConfigs(ForgeConfigSpec.Builder builder) {}

    public boolean isForEvent(Event event) {
        var cls = this.getClass().getAnnotation(Tweak.class).eventClass();
        return cls.toString().equals(event.getClass().toString());
    }
}
