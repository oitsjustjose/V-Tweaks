package com.oitsjustjose.vtweaks.common.tweaks.entity;

import com.oitsjustjose.vtweaks.common.core.Tweak;
import com.oitsjustjose.vtweaks.common.core.VTweak;
import net.minecraft.client.model.BeeModel;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Tweak(category = "client")
public class SmallerBeesTweak extends VTweak {
    private ForgeConfigSpec.BooleanValue enabled;

    @Override
    public void registerConfigs(ForgeConfigSpec.Builder builder) {
        this.enabled = builder.comment("If enabled, all bees will always be half-sized. Does not affect breeding or hitboxes").define("enableSmallBees", true);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void process(RenderLivingEvent.Pre<Bee, BeeModel<Bee>> evt) {
        if (!this.enabled.get()) return;
        if (!(evt.getEntity() instanceof Bee)) return;
        evt.getPoseStack().scale(0.5F, 0.5F, 0.5F);
    }
}
