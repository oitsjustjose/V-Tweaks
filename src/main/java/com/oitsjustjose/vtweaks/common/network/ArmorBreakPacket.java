package com.oitsjustjose.vtweaks.common.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class ArmorBreakPacket {
    public ArmorBreakPacket(PacketBuffer buf) {
    }

    public ArmorBreakPacket() {
    }

    public static ArmorBreakPacket decode(PacketBuffer buf) {
        return new ArmorBreakPacket(buf);
    }

    public static void encode(ArmorBreakPacket msg, PacketBuffer buf) {
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(ArmorBreakPacket msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft.getInstance().player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
            });
        }
        context.get().setPacketHandled(true);
    }
}