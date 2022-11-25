package com.oitsjustjose.vtweaks.common.core;

import net.minecraftforge.eventbus.api.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tweak {
    Class<? extends Event> eventClass();

    String category();
}
