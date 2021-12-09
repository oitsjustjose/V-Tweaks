package com.oitsjustjose.vtweaks.client;

import com.oitsjustjose.vtweaks.common.config.ClientConfig;
import net.minecraft.client.model.BeeModel;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SmallBees {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(RenderLivingEvent.Pre<Bee, BeeModel<Bee>> evt) {
        if (ClientConfig.ENABLE_SMALL_BEES.get() && evt.getEntity() instanceof Bee) {
            evt.getPoseStack().scale(.5F, .5F, .5F);
        }
    }
}
