package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.client.model.BeeModel;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.Event;

@Tweak(eventClass = RenderLivingEvent.Pre.class, category = "client")
public class SmallerBeesTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("If enabled, all bees will always be half-sized. Does not affect breeding or hitboxes").define("enableSmallBees", true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(Event event) {
        if (!this.enabled.get()) return;
        var evt = (RenderLivingEvent.Pre<Bee, BeeModel<Bee>>) event;
        evt.getPoseStack().scale(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean isForEvent(Event event) {
        if (event instanceof RenderLivingEvent.Pre<?, ?> renderEvt) {
            return renderEvt.getEntity() instanceof Bee;
        }
        return super.isForEvent(event);
    }
}
