package com.oitsjustjose.vtweaks.client;

import net.minecraft.client.renderer.entity.model.BeeModel;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SmallBees {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(RenderLivingEvent.Pre<BeeEntity, BeeModel<BeeEntity>> evt) {
        if (evt.getEntity() instanceof BeeEntity) {
            evt.getMatrixStack().scale(.5F, .5F, .5F);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void registerEvent(RenderLivingEvent.Post<BeeEntity, BeeModel<BeeEntity>> evt) {
//        if (evt.getEntity() instanceof BeeEntity) {
//            evt.getMatrixStack().scale(1F, 1F, 1F);
//        }
    }
}
