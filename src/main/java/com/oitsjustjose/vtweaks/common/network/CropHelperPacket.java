package com.oitsjustjose.vtweaks.common.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class CropHelperPacket
{

    public boolean shouldSwing;

    public CropHelperPacket(PacketBuffer buf)
    {
        this.shouldSwing = buf.readBoolean();
    }

    public CropHelperPacket(Boolean swing)
    {
        this.shouldSwing = swing;
    }

    public static CropHelperPacket decode(PacketBuffer buf)
    {
        return new CropHelperPacket(buf);
    }

    public static void encode(CropHelperPacket msg, PacketBuffer buf)
    {
        buf.writeBoolean(msg.shouldSwing);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context)
    {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(CropHelperPacket msg, Supplier<NetworkEvent.Context> context)
    {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            if (msg.shouldSwing)
            {
                context.get().enqueueWork(() -> {
                    Minecraft.getInstance().player.swingArm(Hand.MAIN_HAND);
                });
            }
        }
        context.get().setPacketHandled(true);
    }
}