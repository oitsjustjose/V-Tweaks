package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.client.model.BeeModel;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@Tweak(category = "client.entity")
public class SmallerBeesTweak extends VTweak {
    private ModConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ModConfigSpec.Builder builder) {
        super.registerConfigs(builder);
        this.enabled = builder.comment("Makes Bee entities half-sized (does not affect their hit-box)").define("enableSmallBees", true);
    }

//    @OnlyIn(Dist.CLIENT) // TODO: is this needed?
    @SubscribeEvent
    public void process(RenderLivingEvent.Pre<Bee, BeeModel<Bee>> evt) {
        if (!this.enabled.get()) return;
        if (!(evt.getEntity() instanceof Bee)) return;
        evt.getPoseStack().scale(0.5F, 0.5F, 0.5F);
    }
}
