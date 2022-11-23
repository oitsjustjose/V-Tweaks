package com.oitsjustjose.vtweaks.common.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmorBreakPacket {
    public ArmorBreakPacket(FriendlyByteBuf ignoredBuf) {
    }

    public ArmorBreakPacket() {
    }

    public static ArmorBreakPacket decode(FriendlyByteBuf buf) {
        return new ArmorBreakPacket(buf);
    }

    public static void encode(ArmorBreakPacket ignoredMsg, FriendlyByteBuf ignoredBuf) {
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ArmorBreakPacket ignoredMsg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                var mc = Minecraft.getInstance();
                if (mc.player != null) mc.player.playSound(SoundEvents.SHIELD_BREAK, 1.0F, 1.0F);
            });
        }
        context.get().setPacketHandled(true);
    }
}